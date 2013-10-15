package org.fiteagle.delivery.rest.monitoring;
//package org.fiteagle.delivery.rest.fiteagle;
//
//import static org.junit.Assert.*;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.net.URI;
//import java.net.URISyntaxException;
//
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.UriBuilder;
//
//import junit.framework.Assert;
//
//import org.fiteagle.ui.infinity.InfinityClient;
//import org.junit.Test;
//
//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;
//import com.sun.jersey.api.client.WebResource.Builder;
//import com.sun.jersey.api.client.config.ClientConfig;
//import com.sun.jersey.api.client.config.DefaultClientConfig;
//import com.sun.jersey.core.util.MultivaluedMapImpl;
//
//public class StatusPresenterTest {
//	
//	
//
//	@Test
//	public void testgetStatus() throws URISyntaxException {
//		URI uri = new URI("http://localhost:8080/xipimonitoring/rest");
//		String PATH_PORTLET = "/v1/status";
//		WebResource service=Client.create(new DefaultClientConfig()).resource(uri);;
//		WebResource path=service.path(PATH_PORTLET);
//		
//		MultivaluedMapImpl queryParams=new MultivaluedMapImpl();
//		String jsonString = getJsonString(queryParams, path);
//		System.out.println(jsonString);
//		Assert.assertNotNull(jsonString);
//		
//	}
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
//	@Test
//	public void testGetStatus(){
//		ClientConfig config = new DefaultClientConfig();
//		Client client = Client.create(config);
//		WebResource service = client.resource(getBaseURI());
//		// Fluent interfaces
//		ClientResponse clientResponse = service.path("rest").path("v1/status")
//				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
//		System.out.println(clientResponse.toString());
//		
//		Assert.assertNotNull(clientResponse);
//		
//		String respEntity = clientResponse.getEntity(String.class);
//		System.out.println(respEntity);
//		
//		Assert.assertFalse(respEntity.contains("<title>Apache Tomcat/6.0.37 - Error report</title>"));
//	}
//	
//	private URI getBaseURI() {
//		return UriBuilder.fromUri(
//		// "http://localhost:8080/de.vogella.jersey.first").build();
//				"http://localhost:8080/xipimonitoring").build();
//	}
//	
//	
//
//}
