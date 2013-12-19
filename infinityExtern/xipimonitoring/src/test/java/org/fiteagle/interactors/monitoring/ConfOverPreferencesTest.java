package org.fiteagle.interactors.monitoring;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

public class ConfOverPreferencesTest {

	private MonitoringManager monitoringManager;

	@Before
	public void setup() throws URISyntaxException {
		this.monitoringManager = new MonitoringManager();
	}

	@Test
	public void testIfUtilsSetRight() {
		// change the test values according to set java preferences in OS
		// Assert.assertEquals(Utils.OML_SERVER_HOSTNAME, "localhost");
		// Assert.assertEquals(Utils.PATH_PORTLET,
		// "/InfinityServices-portlet/json");
		// Assert.assertEquals(Utils.XIPI_URI_STRING, "http://www.xipi.eu");
		// Assert.assertEquals(Utils.OML_SERVER_PORT_NUMBER, 3434);
		// Assert.assertEquals(Utils.timeForOldLastCheckedInMilis, 2*60*1000);
		// Assert.assertEquals(Utils.timeForTooOldNotAcceptableLastCheckedInMilis,
		// 2880*60*1000);
		// Assert.assertEquals(Utils.scheduledStatusCheckPeriod, 5);
		// System.out.println(Utils.scheduledStatusCheckPeriod);
	}

}