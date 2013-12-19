package org.fiteagle.delivery.rest.monitoring;

//package org.fiteagle.delivery.rest.fiteagle;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.fiteagle.core.monitoring.StatusTable;
import org.fiteagle.interactors.monitoring.MonitoringManager;
import org.junit.Before;
import org.junit.Test;

public class StatusPresenterTest {

	private StatusPresenter statusPresenter;
	private MonitoringManager monitoringManager;

	@Before
	public void setup() {
		this.statusPresenter = new StatusPresenter();
		this.monitoringManager = new MonitoringManager();
	}

	@Test
	public void testGetStatus() {

		final List<TestbedStatus> status = this.statusPresenter.getStatus();
		Assert.assertNotNull(status);
		Assert.assertFalse(status.isEmpty());
	}

	@Test
	public void testGetTestBedStatusById() {
		this.statusPresenter.getStatus();
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

		final TestbedStatus data = this.statusPresenter
				.getTestBedStatusById("FOKUS FUSECO Playground");
		Assert.assertNotNull(data);
		Assert.assertEquals("FOKUS FUSECO Playground", data.getId());
		Assert.assertEquals("up", data.getStatus());

		final Collection<TestbedStatus> components = data.getComponents();
		final TestbedStatus responseComponent = components.iterator().next();
		Assert.assertNotNull(responseComponent);
		Assert.assertEquals("testComponent", responseComponent.getId());
		Assert.assertEquals("up", data.getStatus());
		Assert.assertEquals(timeInMilis, responseComponent.getLastCheck()
				.getTime());
	}

	// //
	//
	//
	//
	//
	// // @Test
	// // public void testgetStatus() throws URISyntaxException {
	// // URI uri = new URI("http://localhost:8080/xipimonitoring/rest");
	// // String PATH_PORTLET = "/v1/status";
	// // WebResource service=Client.create(new
	// DefaultClientConfig()).resource(uri);;
	// // WebResource path=service.path(PATH_PORTLET);
	// //
	// // MultivaluedMapImpl queryParams=new MultivaluedMapImpl();
	// // String jsonString = getJsonString(queryParams, path);
	// // System.out.println(jsonString);
	// // Assert.assertNotNull(jsonString);
	// //
	// // }
	//
	//
	// private String getJsonString(MultivaluedMapImpl queryParams, WebResource
	// path) {
	// WebResource resource = path.queryParams(queryParams);
	// Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
	// String jsonString = builder.get(String.class);
	// return jsonString;
	// }
	//
	//
	// // @Test
	// // public void testGetStatus(){
	// // ClientConfig config = new DefaultClientConfig();
	// // Client client = Client.create(config);
	// // WebResource service = client.resource(getBaseURI());
	// // // Fluent interfaces
	// // ClientResponse clientResponse = service.path("rest").path("v1/status")
	// // .accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
	// // System.out.println(clientResponse.toString());
	// //
	// // Assert.assertNotNull(clientResponse);
	// //
	// // String respEntity = clientResponse.getEntity(String.class);
	// // System.out.println(respEntity);
	// //
	// //
	// Assert.assertFalse(respEntity.contains("<title>Apache Tomcat/6.0.37 - Error report</title>"));
	// // }
	//
	// // @Test
	// // public void testGetStatus(){
	// //
	// // List<TestbedStatus> status = statusPresenter.getStatus();
	// // Assert.assertNotNull(status);
	// // Assert.assertFalse(status.isEmpty());
	// //
	// //// ClientConfig config = new DefaultClientConfig();
	// //// Client client = Client.create(config);
	// //// WebResource service = client.resource(getBaseURI());
	// //// // Fluent interfaces
	// //// ClientResponse clientResponse =
	// service.path("rest").path("v1/status")
	// //// .accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
	// //// System.out.println(clientResponse.toString());
	// ////
	// //// Assert.assertNotNull(clientResponse);
	// ////
	// //// String respEntity = clientResponse.getEntity(String.class);
	// //// System.out.println(respEntity);
	// ////
	// ////
	// Assert.assertFalse(respEntity.contains("<title>Apache Tomcat/6.0.37 - Error report</title>"));
	// // }
	// //
	// // @Test
	// // public void testGetTestBedStatusById(){
	// //
	// // }
	//
	//
	// private URI getBaseURI() {
	// return UriBuilder.fromUri(
	// // "http://localhost:8080/de.vogella.jersey.first").build();
	// "http://localhost:8080/xipimonitoring").build();
	// }
	//
	//

}
