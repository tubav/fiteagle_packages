//package org.fiteagle.delivery.rest.fiteagle;
//
//import java.net.URI;
//
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.UriBuilder;
//
//import org.junit.Test;
//
//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;
//import com.sun.jersey.api.client.config.ClientConfig;
//import com.sun.jersey.api.client.config.DefaultClientConfig;
//
//public class HelloTest {
//
//	@Test
//	public void testHello() {
//
//		ClientConfig config = new DefaultClientConfig();
//		Client client = Client.create(config);
//		WebResource service = client.resource(getBaseURI());
//		// Fluent interfaces
//		ClientResponse clientResponse = service.path("rest").path("hello")
//				.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);
//		System.out.println(clientResponse.toString());
//		System.out.println(clientResponse.getEntity(String.class));
//		// // Get plain text
//		// System.out.println(service.path("rest").path("hello")
//		// .accept(MediaType.TEXT_PLAIN).get(String.class));
//		// // Get XML
//		// System.out.println(service.path("rest").path("hello")
//		// .accept(MediaType.TEXT_XML).get(String.class));
//		// // The HTML
//		// System.out.println(service.path("rest").path("hello")
//		// .accept(MediaType.TEXT_HTML).get(String.class));
//
//	}
//
//	private URI getBaseURI() {
//		return UriBuilder.fromUri(
//		// "http://localhost:8080/de.vogella.jersey.first").build();
//				"http://localhost:8080/xipimonitoring").build();
//	}
//
//}