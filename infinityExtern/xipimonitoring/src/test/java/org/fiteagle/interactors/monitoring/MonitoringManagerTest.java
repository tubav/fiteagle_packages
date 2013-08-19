package org.fiteagle.interactors.monitoring;

import static org.junit.Assert.*;

import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import org.fiteagle.ui.infinity.InfinityClient;
import org.fiteagle.ui.infinity.InfinityClientMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.fiteagle.core.monitoring.StatusTable;

import com.sun.jersey.server.impl.managedbeans.ManagedBeanComponentProviderFactoryInitilizer;

public class MonitoringManagerTest {

	private MonitoringManager monitoringManager;

	@Before
	public void setup() throws URISyntaxException {
		this.monitoringManager = new MonitoringManager();
	}
	
	@Test
	public void testGetXIPIMonitoringData() throws URISyntaxException {
		List<StatusTable> result = monitoringManager.getXIPIMonitoringData();
		Assert.assertNotNull(result);
		
	}

	@Test
	public void getMonitoringDataById(){
//		monitoringManager.getXIPIMonitoringData();
//		StatusTable statusTable = new StatusTable();
//		statusTable.setId("FOKUS FUSECO Playground");
//		statusTable.setLastCheck(new Date());
//		statusTable.setStatus("up");
//		StatusTable component= new StatusTable();
//		component.setId("componentId");
//		component.setStatus("up");
//		component.setLastCheck(new Date());
//		statusTable.addComponent(component);
//		
//		monitoringManager.pushMonitoringData(statusTable);
//		StatusTable data = monitoringManager.getMonitoringDataById("FOKUS FUSECO Playground");
//		Assert.assertNull(data);
//		Assert.assertEquals("FOKUS FUSECO Playground", data.getId());
//		Assert.assertEquals("up", data.getStatus());
	}
	
}
