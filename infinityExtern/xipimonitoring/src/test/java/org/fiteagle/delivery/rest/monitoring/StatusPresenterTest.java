package org.fiteagle.delivery.rest.monitoring;
//package org.fiteagle.delivery.rest.fiteagle;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import junit.framework.Assert;

import org.fiteagle.core.monitoring.StatusTable;
import org.fiteagle.interactors.monitoring.MonitoringManager;
import org.fiteagle.ui.infinity.InfinityClient;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class StatusPresenterTest {
	
	private StatusPresenter statusPresenter;
	private MonitoringManager monitoringManager;
	
	@Before
	public void setup(){
		this.statusPresenter = new StatusPresenter();
		this.monitoringManager = new MonitoringManager();
	}
	
	@Test
	public void testGetStatus(){
		
		List<TestbedStatus> status = statusPresenter.getStatus();
		Assert.assertNotNull(status);
		Assert.assertFalse(status.isEmpty());
	}
	
	@Test
	public void testGetTestBedStatusById(){
		statusPresenter.getStatus();
		StatusTable statusTable = new StatusTable();
		statusTable.setId("FOKUS FUSECO Playground");
		statusTable.setLastCheck(new Date());
		statusTable.setStatus("up");
		
		StatusTable component= new StatusTable();
		component.setId("componentId");
		component.setStatus("up");
		Date date = new Date();
		long timeInMilis = date.getTime();
		component.setLastCheck(date);
		statusTable.addComponent(component);
		monitoringManager.pushMonitoringData(statusTable);
		
		
//		StatusTable data = monitoringManager.getMonitoringDataById("FOKUS FUSECO Playground");
		TestbedStatus data = statusPresenter.getTestBedStatusById("FOKUS FUSECO Playground");
		Assert.assertNotNull(data);
		Assert.assertEquals("FOKUS FUSECO Playground", data.getId());
		Assert.assertEquals("up", data.getStatus());

		Collection<TestbedStatus> components = data.getComponents();
		TestbedStatus responseComponent = components.iterator().next();
		Assert.assertNotNull(responseComponent);
		Assert.assertEquals("componentId", responseComponent.getId());
		Assert.assertEquals("up", data.getStatus());
		Assert.assertEquals(timeInMilis, responseComponent.getLastCheck().getTime());
	}
	
	
	
	
	
	
	
	
	
//	//
//	
//	
//	
//
////	@Test
////	public void testgetStatus() throws URISyntaxException {
////		URI uri = new URI("http://localhost:8080/xipimonitoring/rest");
////		String PATH_PORTLET = "/v1/status";
////		WebResource service=Client.create(new DefaultClientConfig()).resource(uri);;
////		WebResource path=service.path(PATH_PORTLET);
////		
////		MultivaluedMapImpl queryParams=new MultivaluedMapImpl();
////		String jsonString = getJsonString(queryParams, path);
////		System.out.println(jsonString);
////		Assert.assertNotNull(jsonString);
////		
////	}
//	
//	
//	private String getJsonString(MultivaluedMapImpl queryParams, WebResource path) {
//		WebResource resource = path.queryParams(queryParams);
//		Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
//		String jsonString = builder.get(String.class);
//		return jsonString;
//	}
//	
//	
////	@Test
////	public void testGetStatus(){
////		ClientConfig config = new DefaultClientConfig();
////		Client client = Client.create(config);
////		WebResource service = client.resource(getBaseURI());
////		// Fluent interfaces
////		ClientResponse clientResponse = service.path("rest").path("v1/status")
////				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
////		System.out.println(clientResponse.toString());
////		
////		Assert.assertNotNull(clientResponse);
////		
////		String respEntity = clientResponse.getEntity(String.class);
////		System.out.println(respEntity);
////		
////		Assert.assertFalse(respEntity.contains("<title>Apache Tomcat/6.0.37 - Error report</title>"));
////	}
//	
////	@Test
////	public void testGetStatus(){
////		
////		List<TestbedStatus> status = statusPresenter.getStatus();
////		Assert.assertNotNull(status);
////		Assert.assertFalse(status.isEmpty());
////		
//////		ClientConfig config = new DefaultClientConfig();
//////		Client client = Client.create(config);
//////		WebResource service = client.resource(getBaseURI());
//////		// Fluent interfaces
//////		ClientResponse clientResponse = service.path("rest").path("v1/status")
//////				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
//////		System.out.println(clientResponse.toString());
//////		
//////		Assert.assertNotNull(clientResponse);
//////		
//////		String respEntity = clientResponse.getEntity(String.class);
//////		System.out.println(respEntity);
//////		
//////		Assert.assertFalse(respEntity.contains("<title>Apache Tomcat/6.0.37 - Error report</title>"));
////	}
////	
////	@Test
////	public void testGetTestBedStatusById(){
////		
////	}
//	
//	
//	private URI getBaseURI() {
//		return UriBuilder.fromUri(
//		// "http://localhost:8080/de.vogella.jersey.first").build();
//				"http://localhost:8080/xipimonitoring").build();
//	}
//	
//	

}
