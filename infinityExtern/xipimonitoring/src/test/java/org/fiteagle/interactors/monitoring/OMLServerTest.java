package org.fiteagle.interactors.monitoring;

import static org.junit.Assert.*;

import java.net.URISyntaxException;

import org.fiteagle.interactors.monitoring.client.OMLClientMock;
import org.fiteagle.interactors.monitoring.server.OMLServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OMLServerTest {
	
	OMLClientMock client;
	OMLServer omlServer;
	
	@Before
	public void setup() {
//		omlServer = new OMLServer();
		client = new OMLClientMock();
//		omlServer.run();
	}

	@Test
	public void testPushMonitoringData() {
		client.run();
	}
	
	@Test
	public void testPushMonitoringFromAFile() {
//		String hostName="localhost";
//		int portNumber=3434;
//		String pathToFile = "/path/to/OMLStreamFile.txt";
//		client = new OMLClientMock(hostName, portNumber, pathToFile);
//		client.run();
	}
	
	@After
	public void teardown(){
//		omlServer.terminate();
	}
	

}
