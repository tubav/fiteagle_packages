package org.fiteagle.interactors.monitoring;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import org.fiteagle.core.monitoring.StatusTable;
import org.fiteagle.interactors.api.ResourceMonitoringBoundary;
import org.fiteagle.interactors.monitoring.server.OMLServer;
import org.fiteagle.ui.infinity.InfinityClient;
import org.fiteagle.ui.infinity.InfinityClientWeb;
import org.fiteagle.ui.infinity.model.InfinityValueID;

public class MonitoringManager implements ResourceMonitoringBoundary {

	private final static Logger LOGGER = Logger
			.getLogger(org.fiteagle.interactors.monitoring.MonitoringManager.class
					.getName());

	private static Map<String, StatusTable> monitoringData = new HashMap<String, StatusTable>();

	private static boolean serverStarted = false;
	private static OMLServer omlServer = new OMLServer();

	InfinityClient client;

	public MonitoringManager() {
		if (!MonitoringManager.isServerStarted()) {
			this.setUtils();

			this.startServerAsThread();
			MonitoringManager.setServerStarted(true);
			this.startRegularStatusCheck();
		}
	}

	private void startServerAsThread() {
		new Thread(MonitoringManager.omlServer).start();
	}

	private void startRegularStatusCheck() {
		final ScheduledExecutorService statusCheckService = Executors
				.newScheduledThreadPool(1);
		final Runnable service = new Runnable() {
			@Override
			public void run() {
				final TestbedStatusCheck statusCheck = new TestbedStatusCheck();
				if ((MonitoringManager.monitoringData != null)
						&& !MonitoringManager.monitoringData.isEmpty()) {
					final Collection<StatusTable> testbeds = MonitoringManager.monitoringData
							.values();
					for (final Object element : testbeds) {
						StatusTable testbed = (StatusTable) element;
						if (testbed.getStatus()
								.compareTo(StatusTable.UNDEFINED) == 0) {
							continue;
						}
						testbed = statusCheck
								.updateComponentsOfTestbed(testbed);
						testbed = statusCheck.updateStatusTableState(testbed);
						MonitoringManager.this.pushMonitoringData(testbed);
					}
				}
			}
		};
		MonitoringManager.LOGGER.setLevel(Level.INFO);
		MonitoringManager.LOGGER
				.info("Starting scheduled status check with time period: "
						+ Utils.scheduledStatusCheckPeriod + " minutes");
		statusCheckService.scheduleAtFixedRate(service,
				Utils.scheduledStatusCheckPeriod,
				Utils.scheduledStatusCheckPeriod, TimeUnit.MINUTES);
	}

	private void setUtils() {
		final Preferences preferences = Preferences.userNodeForPackage(this
				.getClass());

		if (preferences.get("XIPI_URI_STRING", null) == null) {
			preferences.put("XIPI_URI_STRING", Utils.xipiURIString);
		} else {
			Utils.xipiURIString = preferences.get("XIPI_URI_STRING", null);
		}

		if (preferences.get("OML_SERVER_HOSTNAME", null) == null) {
			preferences
					.put("OML_SERVER_HOSTNAME", Utils.getOmlServerHostName());
		} else {
			Utils.setOmlServerHostName(preferences.get("OML_SERVER_HOSTNAME",
					null));
		}

		if (preferences.get("PATH_PORTLET", null) == null) {
			preferences.put("PATH_PORTLET", Utils.getPathPortlet());
		} else {
			Utils.setPathPortlet(preferences.get("PATH_PORTLET", null));
		}

		if (preferences.get("OML_SERVER_PORT_NUMBER", null) == null) {
			preferences.put("OML_SERVER_PORT_NUMBER",
					Integer.valueOf(Utils.getOmlServerPortNumber()).toString());
		} else {
			Utils.setOmlServerPortNumber(Integer.parseInt(preferences.get(
					"OML_SERVER_PORT_NUMBER", null)));
		}

		if (preferences.get("TIME_FOR_LAST_CHECKED_OLD", null) == null) {
			preferences.put("TIME_FOR_LAST_CHECKED_OLD",
					this.milisInMinutes(Utils.timeForOldLastCheckedInMilis)
							.toString());
		} else {
			Utils.timeForOldLastCheckedInMilis = Long.parseLong(preferences
					.get("TIME_FOR_LAST_CHECKED_OLD", null)) * 60000L;
		}

		if (preferences.get("TIME_FOR_LAST_CHECKED_TOO_OLD", null) == null) {
			preferences.put(
					"TIME_FOR_LAST_CHECKED_TOO_OLD",
					this.milisInMinutes(
							Utils.timeForTooOldNotAcceptableLastCheckedInMilis)
							.toString());
		} else {
			Utils.timeForTooOldNotAcceptableLastCheckedInMilis = Long
					.parseLong(preferences.get("TIME_FOR_LAST_CHECKED_TOO_OLD",
							null)) * 60000L;
		}

		if (preferences.get("SCHEDULED_STATUS_CHECK_PERIOD", null) == null) {
			preferences.put("SCHEDULED_STATUS_CHECK_PERIOD",
					Integer.valueOf(Utils.scheduledStatusCheckPeriod)
							.toString());
		} else {
			Utils.scheduledStatusCheckPeriod = Integer.parseInt(preferences
					.get("SCHEDULED_STATUS_CHECK_PERIOD", null));
		}
	}

	private Long milisInMinutes(final long milis) {
		final long minutes = milis / 60000L;
		return Long.valueOf(minutes);
	}

	public void terminateOMLServer() {
		MonitoringManager.omlServer.terminate();
	}

	@Override
	public Collection<StatusTable> getMonitoringData() {
		if (MonitoringManager.monitoringData.isEmpty()) {
			List<StatusTable> data;
			try {
				data = this.getXIPIMonitoringData();
			} catch (final URISyntaxException e) {
				throw new RuntimeException(e);
			}
			this.addMonitoringData(data);
		}
		return MonitoringManager.monitoringData.values();
	}

	private void addMonitoringData(final List<StatusTable> data) {
		for (final Object element : data) {
			final StatusTable statusTable = (StatusTable) element;
			MonitoringManager.monitoringData.put(statusTable.getId(),
					statusTable);
		}
	}

	List<StatusTable> getXIPIMonitoringData() throws URISyntaxException {

		this.client = new InfinityClientWeb(new URI(Utils.xipiURIString));

		final ArrayList<InfinityValueID> infrastructures = (ArrayList<InfinityValueID>) this.client
				.searchInfrastructures();

		final ArrayList<StatusTable> result = new ArrayList<StatusTable>();

		if (infrastructures == null) {
			return null;
		}

		for (final Object element : infrastructures) {
			final InfinityValueID infinityValueID = (InfinityValueID) element;

			final StatusTable statusTable = new StatusTable();

			final String id = infinityValueID.getId();
			statusTable.setXipiId(id);
			statusTable.setId(infinityValueID.getValue());

			// status does not give information about the status of testbed or
			// component => do not get the infrastructures one by one!!
			// InfinityInfrastructure infinityInfrastructure =
			// getInfrastuctureByID(new Integer(
			// id));
			//
			// if (infinityInfrastructure.getStatus() != null
			// && infinityInfrastructure.getStatus() != "") {
			// } else

			statusTable.setStatus(StatusTable.UNDEFINED);

			result.add(statusTable);
		}

		return result;
	}

	public final void pushMonitoringData(final StatusTable statusTable) {
		if (MonitoringManager.monitoringData.get(statusTable.getId()) != null) {
			MonitoringManager.monitoringData.put(statusTable.getId(),
					statusTable);
		}
	}

	@Override
	public StatusTable getMonitoringDataById(final String id) {
		return MonitoringManager.monitoringData.get(id);
	}

	public void reset() {
		this.terminateOMLServer();
		this.resetMonitoringData();
		this.resetOMLServer();
	}

	private void resetOMLServer() {
		MonitoringManager.setServerStarted(false);
		MonitoringManager.omlServer = new OMLServer();
	}

	private void resetMonitoringData() {
		MonitoringManager.monitoringData = new HashMap<String, StatusTable>();
	}

	public static boolean isServerStarted() {
		return MonitoringManager.serverStarted;
	}

	public static void setServerStarted(final boolean serverStarted) {
		MonitoringManager.serverStarted = serverStarted;
	}

}
