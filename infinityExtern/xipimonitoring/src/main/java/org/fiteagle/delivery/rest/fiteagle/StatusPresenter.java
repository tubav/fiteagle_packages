package org.fiteagle.delivery.rest.fiteagle;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.fiteagle.interactors.api.ResourceMonitoringBoundary;
import org.fiteagle.interactors.monitoring.MonitoringManager;

import org.fiteagle.core.monitoring.StatusTable;

@Path("/v1/status/")
public class StatusPresenter {

	private ResourceMonitoringBoundary monitor = new MonitoringManager();

//	@Inject
//	public StatusPresenter(final ResourceMonitoringBoundary monitor) {
//		this.monitor = monitor;
//	}

	@GET
	@Path("")
	@Produces("application/json")
	public List<TestbedStatus> getStatus() {
		
		ArrayList<TestbedStatus> status = new ArrayList<TestbedStatus>(); 
		
		Collection<StatusTable> monitoringData = monitor.getMonitoringData();
		
		for (Iterator iterator = monitoringData.iterator(); iterator.hasNext();) {
			StatusTable statusTable = (StatusTable) iterator.next();
			status.add(statusTable2Testbedstatus(statusTable));
		}
		
		return status;

	}

	@GET
	@Path("getById")
//	@Path("{id}")
	@Produces("application/json")
//	public TestbedStatus getTestBedStatusById(@PathParam("id") String id) {
	public TestbedStatus getTestBedStatusById(@QueryParam("id") String id) {
//		String decodedId=null;
//		try {
//			decodedId = URLDecoder.decode(id, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return statusTable2Testbedstatus(monitor.getMonitoringDataById(id));
//		return statusTable2Testbedstatus(monitor.getMonitoringDataById(decodedId));
	}
	
	private TestbedStatus statusTable2Testbedstatus(StatusTable statusTable){
		if(statusTable==null) return null;
		TestbedStatus testbedStatus = new TestbedStatus();
		
		testbedStatus.setId(statusTable.getId());
		testbedStatus.setLastCheck(statusTable.getLastCheck());
		testbedStatus.setStatus(statusTable.getStatus());
		
		if(statusTable.getComponents()!=null){
			Collection<StatusTable> components = statusTable.getComponents();
			for (Iterator iterator = components.iterator(); iterator.hasNext();) {
				StatusTable statusTable2 = (StatusTable) iterator.next();
				testbedStatus.addComponent(statusTable2Testbedstatus(statusTable2));
			}
		}
		
		return testbedStatus;
		
	}

}
