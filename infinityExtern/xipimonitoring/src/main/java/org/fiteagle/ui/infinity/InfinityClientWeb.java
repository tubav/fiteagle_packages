package org.fiteagle.ui.infinity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;

import javax.ws.rs.core.MediaType;

import org.fiteagle.ui.infinity.model.InfinityArrayList;
import org.fiteagle.ui.infinity.model.InfinityInfrastructure;
import org.fiteagle.ui.infinity.model.InfinityValueID;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class InfinityClientWeb extends InfinityClient {

	private static final String PATH_PORTLET = "/InfinityServices-portlet/json";
	private final WebResource service;
	private final WebResource path;

	public InfinityClientWeb(URI uri) {
		this.service = Client.create(new DefaultClientConfig()).resource(uri);
		this.path = service.path(PATH_PORTLET);
	}

	@Override
	public InfinityInfrastructure getInfrastructuresById(Number id) {
		String methodName = InfinityClient.Methods.GET_INFRA_BY_ID.getValue();
		MultivaluedMapImpl queryParams = getDefaultQueryParams(methodName);
		queryParams.add("serviceParameters", "[id]");
		queryParams.add("id", id.toString());

		String jsonString = getJsonString(queryParams);

		try {
			return this.parser.parseGetInfrastructuresById(jsonString);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ArrayList<InfinityValueID> searchInfrastructures() {
		String methodName = InfinityClient.Methods.SEARCH_INFRASTRUCTURES
				.getValue();
		MultivaluedMapImpl queryParams = getDefaultQueryParams(methodName);
		queryParams.add("serviceParameters", "%5btext,country,component%5d");

		String jsonString = getJsonString(queryParams);

		try {
			return this.parser.parseSearchInfrastructuresResponse(jsonString);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ArrayList<InfinityValueID> getTechnicalComponents() {
		String methodName = InfinityClient.Methods.GET_TECHNICAL_COMPONENTS
				.getValue();
		MultivaluedMapImpl queryParams = getDefaultQueryParams(methodName);
		String jsonString = getJsonString(queryParams);
		try {
			return this.parser.parseGetTechnicalComponentsResponse(jsonString);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ArrayList<InfinityArrayList> getComponentDetail(String infrastructureId, String componentId) {
		String methodName = InfinityClient.Methods.GET_COMPONENT_DETAIL.getValue();
		MultivaluedMapImpl queryParams = getDefaultQueryParams(methodName);
		
//		serviceParameters=[idInfra,idComponent]&idInfra=900&idComponent=242
		queryParams.add("serviceParameters", "%5bidInfra,idComponent%5d");
		
		if(infrastructureId!=null && infrastructureId !="")
			queryParams.add("idInfra", infrastructureId);
		
		if(componentId!=null && componentId!="")
			queryParams.add("idComponent", componentId);
		
		String jsonString = getJsonString(queryParams);
		try {
			return this.parser.parseGetComponentDetailResponse(jsonString);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	private String getJsonString(MultivaluedMapImpl queryParams) {
//		WebResource resource = path.queryParams(queryParams);
//		Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
//		String jsonString = builder.get(String.class);
		
		//TEST
		
		WebResource resource = path.queryParams(queryParams);
		ClientResponse clientResponse = resource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
		
		InputStream entityInputStream = clientResponse.getEntityInputStream();
		
//		String jsonString = builder.get(String.class);
		
		//TEST
		
		
		
		
		
//		InputStream jsonString = builder.get(InputStream.class);
		
//		//TODO: To test only
//		String encoding = "";
//		try {
//			encoding = GetCharset.guessEncoding(GetCharset.getBytesOfInputStream(jsonString));
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
//		String str=null;
//		try {
//			str = new String(jsonString.getBytes("cp1252"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return str;

		InputStream fixedInputStream = null;
		
		
		
		// try {
		// fixedInputStream = fixEncoding(new
		// ByteArrayInputStream(jsonString.getBytes("ISO-8859-1")));
		
		
		InputStream inputStream=null;
		
		
//		try {
////			inputStream = new ByteArrayInputStream(jsonString.getBytes("ISO-8859-1"));
//			inputStream = new ByteArrayInputStream(jsonString.getBytes("WINDOWS-1252"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		//TO test
//		try {
//			inputStream = new ByteArrayInputStream(jsonString.getBytes("cp1252"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String respo = fixXIPIEncodingStr(inputStream);
//		return respo;
		
		
//		String respo = fixXIPIEncodingStr(entityInputStream);
		InputStream inpuster = fixXIPIEncoding(entityInputStream);
		String respo = convertStreamToString(inpuster);
		return respo;
		
		//7totest
		
		
//		fixedInputStream = fixXIPIEncoding(inputStream);
////		fixedInputStream = fixXIPIEncoding(jsonString);
//		
//		
//		//--TESTING
////		fixedInputStream = new String(jsonString.getBytes(), "ISO-8859-1").getBytes("UTF-8");
//		
//		
////		try {
//////			fixedInputStream = new String(jsonString.getBytes(), "ISO-8859-1").getBytes("UTF-8");
//////			fixedInputStream = new String(GetCharset.getBytesOfInputStream(jsonString) ,"ISO-8859-1").getBytes("UTF-8");
////		} catch (UnsupportedEncodingException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		return fixXIPIEncodingStr(jsonString);
//		return convertStreamToString(fixedInputStream);
		
		
		
		//--/TESTING
		
		
		
//		 fixedInputStream = fixXIPIEncodingTest(new
//		 ByteArrayInputStream(jsonString.getBytes()));
//		 } catch (UnsupportedEncodingException e) {
//		 throw new RuntimeException(e.getMessage());
//		 }

		// String result = fixXIPIEncodingTest(new
		// ByteArrayInputStream(jsonString.getBytes()));
		
//		return convertStreamToString(new ByteArrayInputStream(
//				jsonString.getBytes()));
//		return convertStreamToString(fixedInputStream);
//		return "something";
		
		
		// return result;
		
		
	}

	private MultivaluedMapImpl getDefaultQueryParams(String methodName) {
		MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
		queryParams.add("serviceClassName",
				"com.liferay.infinity.service.InfrastructureServiceUtil");
		queryParams.add("serviceMethodName", methodName);
		return queryParams;
	}


}
