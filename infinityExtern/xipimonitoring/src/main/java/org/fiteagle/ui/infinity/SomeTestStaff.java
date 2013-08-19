package org.fiteagle.ui.infinity;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
public class SomeTestStaff {
	
	
	public String getStatus(String uriStr) throws URISyntaxException{	
	URI uri = new URI(uriStr);
	String PATH_PORTLET = "/v1/status";
	WebResource service = Client.create(new DefaultClientConfig()).resource(uri);
	WebResource path = service.path(PATH_PORTLET);
	MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
	WebResource resource = path.queryParams(queryParams);
	Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
	String jsonString = builder.get(String.class);
	return jsonString;
	}

}
