package org.fiteagle.interactors.monitoring;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.prefs.Preferences;

import org.fiteagle.interactors.api.ResourceMonitoringBoundary;
import org.fiteagle.interactors.monitoring.server.OMLServer;
import org.fiteagle.ui.infinity.InfinityClient;
import org.fiteagle.ui.infinity.InfinityClientMock;
import org.fiteagle.ui.infinity.InfinityClientWeb;
import org.fiteagle.ui.infinity.model.InfinityInfrastructure;
import org.fiteagle.ui.infinity.model.InfinityValueID;
import org.fiteagle.core.monitoring.StatusTable;

import com.sun.xml.bind.Util;

public class MonitoringManager implements ResourceMonitoringBoundary {

	private static HashMap<String, StatusTable> monitoringData = new HashMap<String, StatusTable>();

	private static boolean serverStarted = false;
	private static OMLServer omlServer = new OMLServer();

	// InfinityClientMock client = new InfinityClientMock();
	InfinityClient client;

	public MonitoringManager() {
		if (!serverStarted) {
			setUtils();

			new Thread(omlServer).start();
			serverStarted = true;
		}
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
			Utils.OML_SERVER_HOSTNAME = preferences.get("OML_SERVER_HOSTNAME", null);
		
		if (preferences.get("PATH_PORTLET", null) == null)
			preferences.put("PATH_PORTLET", Utils.PATH_PORTLET);
		else
			Utils.PATH_PORTLET = preferences.get("PATH_PORTLET", null);
		
		if (preferences.get("OML_SERVER_PORT_NUMBER", null) == null)
			preferences.put("OML_SERVER_PORT_NUMBER", new Integer(Utils.OML_SERVER_PORT_NUMBER).toString());
		else
			Utils.OML_SERVER_PORT_NUMBER= Integer.parseInt(preferences.get("OML_SERVER_PORT_NUMBER", null));
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

			//status does not give information about the status of testbed or component => do not get the infrastructures one by one!!
//			InfinityInfrastructure infinityInfrastructure = getInfrastuctureByID(new Integer(
//					id));
//
//			if (infinityInfrastructure.getStatus() != null
//					&& infinityInfrastructure.getStatus() != "") {
//			} else
			
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
