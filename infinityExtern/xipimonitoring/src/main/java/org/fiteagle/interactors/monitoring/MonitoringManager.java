package org.fiteagle.interactors.monitoring;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
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
import org.fiteagle.ui.infinity.model.InfinityInfrastructure;
import org.fiteagle.ui.infinity.model.InfinityValueID;

public class MonitoringManager implements ResourceMonitoringBoundary {
	
	private final static Logger LOGGER = Logger.getLogger(org.fiteagle.interactors.monitoring.MonitoringManager.class.getName()); 

	private static HashMap<String, StatusTable> monitoringData = new HashMap<String, StatusTable>();

	private static boolean serverStarted = false;
	private static OMLServer omlServer = new OMLServer();

//	int scheduledStatusCheckPeriod = Utils.scheduledStatusCheckPeriod;

	// InfinityClientMock client = new InfinityClientMock();
	InfinityClient client;

	public MonitoringManager() {
		if (!serverStarted) {
			setUtils();

			new Thread(omlServer).start();
			serverStarted = true;
			startRegularStatusCheck();
		}
	}

	private void startRegularStatusCheck() {
		ScheduledExecutorService statusCheckService = Executors
				.newScheduledThreadPool(1);
		Runnable service = new Runnable() {
			public void run() {
				TestbedStatusCheck statusCheck = new TestbedStatusCheck();
				if (monitoringData != null && monitoringData.size() > 0) {
					Collection<StatusTable> testbeds = monitoringData.values();
					for (Iterator iterator = testbeds.iterator(); iterator
							.hasNext();) {
						StatusTable testbed = (StatusTable) iterator.next();
						if(testbed.getStatus().compareTo(StatusTable.UNDEFINED)==0) continue;
						testbed = statusCheck
								.updateComponentsOfTestbed(testbed);
						testbed = statusCheck.updateStatusTableState(testbed);
						pushMonitoringData(testbed);
					}
				}
			}
		};
		LOGGER.setLevel(Level.INFO);
		LOGGER.info("Derived scheduled status check period is: "+Utils.scheduledStatusCheckPeriod);
		statusCheckService.scheduleAtFixedRate(service,
				Utils.scheduledStatusCheckPeriod, Utils.scheduledStatusCheckPeriod,
				TimeUnit.MINUTES);
	}

	private void setUtils() {
		Preferences preferences = Preferences.userNodeForPackage(getClass());

		if (preferences.get("XIPI_URI_STRING", null) == null)
			preferences.put("XIPI_URI_STRING", Utils.XIPI_URI_STRING);
		else
			Utils.XIPI_URI_STRING = preferences.get("XIPI_URI_STRING", null);

		if (preferences.get("OML_SERVER_HOSTNAME", null) == null)
			preferences.put("OML_SERVER_HOSTNAME", Utils.OML_SERVER_HOSTNAME);
		else
			Utils.OML_SERVER_HOSTNAME = preferences.get("OML_SERVER_HOSTNAME",
					null);

		if (preferences.get("PATH_PORTLET", null) == null)
			preferences.put("PATH_PORTLET", Utils.PATH_PORTLET);
		else
			Utils.PATH_PORTLET = preferences.get("PATH_PORTLET", null);

		if (preferences.get("OML_SERVER_PORT_NUMBER", null) == null)
			preferences.put("OML_SERVER_PORT_NUMBER", new Integer(
					Utils.OML_SERVER_PORT_NUMBER).toString());
		else
			Utils.OML_SERVER_PORT_NUMBER = Integer.parseInt(preferences.get(
					"OML_SERVER_PORT_NUMBER", null));
		
		if (preferences.get("TIME_FOR_LAST_CHECKED_OLD", null) == null)
			preferences.put("TIME_FOR_LAST_CHECKED_OLD", milisInMinutes(
					Utils.timeForOldLastCheckedInMilis).toString());
		else
			Utils.timeForOldLastCheckedInMilis = Long.parseLong(preferences.get(
					"TIME_FOR_LAST_CHECKED_OLD", null))*60000L;
		
		if (preferences.get("TIME_FOR_LAST_CHECKED_TOO_OLD", null) == null)
			preferences.put("TIME_FOR_LAST_CHECKED_TOO_OLD", milisInMinutes(
					Utils.timeForTooOldNotAcceptableLastCheckedInMilis).toString());
		else
			Utils.timeForTooOldNotAcceptableLastCheckedInMilis = Long.parseLong(preferences.get(
					"TIME_FOR_LAST_CHECKED_TOO_OLD", null))*60000L;
		
		
		if (preferences.get("SCHEDULED_STATUS_CHECK_PERIOD", null) == null)
			preferences.put("SCHEDULED_STATUS_CHECK_PERIOD", new Integer(
					Utils.scheduledStatusCheckPeriod).toString());
		else
			Utils.scheduledStatusCheckPeriod = Integer.parseInt(preferences.get(
					"SCHEDULED_STATUS_CHECK_PERIOD", null));
	}

	private Long milisInMinutes(long milis) {
		long minutes = milis/60000L;
		return new Long(minutes);
	}

	public void terminateOMLServer() {
		omlServer.terminate();
	}

	public Collection<StatusTable> getMonitoringData() {
		if (monitoringData.isEmpty()) {
			List<StatusTable> data;
			try {
				data = getXIPIMonitoringData();
			} catch (URISyntaxException e) {
				throw new RuntimeException(e.getMessage());
			}
			addMonitoringData(data);
		}
		return monitoringData.values();
	}

	private void addMonitoringData(List<StatusTable> data) {
		for (Iterator iterator = data.iterator(); iterator.hasNext();) {
			StatusTable statusTable = (StatusTable) iterator.next();
			monitoringData.put(statusTable.getId(), statusTable);
		}
	}

	List<StatusTable> getXIPIMonitoringData() throws URISyntaxException {

		client = new InfinityClientWeb(new URI(Utils.XIPI_URI_STRING));
		// client = new InfinityClientMock();

		ArrayList<InfinityValueID> infrastructures = client
				.searchInfrastructures();

		ArrayList<StatusTable> result = new ArrayList<StatusTable>();

		if (infrastructures == null)
			return null;

		for (Iterator iterator = infrastructures.iterator(); iterator.hasNext();) {
			InfinityValueID infinityValueID = (InfinityValueID) iterator.next();

			StatusTable statusTable = new StatusTable();

			String id = infinityValueID.getId();
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

	private InfinityInfrastructure getInfrastuctureByID(Number id) {
		return client.getInfrastructuresById(id);
	}

	public void pushMonitoringData(StatusTable statusTable) {
		if (monitoringData.get(statusTable.getId()) != null)
			monitoringData.put(statusTable.getId(), statusTable);
	}

	public StatusTable getMonitoringDataById(String id) {
		return monitoringData.get(id);
	}

}
