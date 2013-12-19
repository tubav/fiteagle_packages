package org.fiteagle.interactors.monitoring;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.fiteagle.core.monitoring.StatusTable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MonitoringManagerTest {

	private MonitoringManager monitoringManager;

	@Before
	public void setup() throws URISyntaxException {
		this.monitoringManager = new MonitoringManager();
	}

	@Test
	public void testGetXIPIMonitoringData() throws URISyntaxException {
		final List<StatusTable> result = this.monitoringManager
				.getXIPIMonitoringData();
		Assert.assertNotNull(result);
		Assert.assertFalse(result.isEmpty());

	}

	@Test
	public void getMonitoringDataById() throws URISyntaxException {
		this.monitoringManager.getMonitoringData();
		final StatusTable statusTable = new StatusTable();
		statusTable.setId("FOKUS FUSECO Playground");

		statusTable.setLastCheck(new Date());
		statusTable.setStatus("up");

		final StatusTable component = new StatusTable();
		component.setId("testComponent");
		component.setStatus("up");

		final Date date = new Date();
		final long timeInMilis = date.getTime();
		component.setLastCheck(date);

		statusTable.addComponent(component);

		this.monitoringManager.pushMonitoringData(statusTable);
		final StatusTable data = this.monitoringManager
				.getMonitoringDataById("FOKUS FUSECO Playground");
		Assert.assertNotNull(data);
		Assert.assertEquals("FOKUS FUSECO Playground", data.getId());
		Assert.assertEquals("up", data.getStatus());

		final Collection<StatusTable> components = data.getComponents();
		final StatusTable responseComponent = components.iterator().next();
		Assert.assertNotNull(responseComponent);
		Assert.assertEquals("testComponent", responseComponent.getId());
		Assert.assertEquals("up", data.getStatus());
		Assert.assertEquals(timeInMilis, responseComponent.getLastCheck()
				.getTime());

	}

}
