package org.fiteagle.ui.infinity;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.fiteagle.interactors.monitoring.Utils;
import org.fiteagle.ui.infinity.model.InfinityArrayList;
import org.fiteagle.ui.infinity.model.InfinityInfrastructure;
import org.fiteagle.ui.infinity.model.InfinityValueID;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class InfinityClientWeb extends InfinityClient {

	private final WebResource path;

	public InfinityClientWeb(final URI uri) {
		final WebResource service = Client.create(new DefaultClientConfig())
				.resource(uri);
		this.path = service.path(Utils.getPathPortlet());
	}

	@Override
	public InfinityInfrastructure getInfrastructuresById(final Number id) {
		final String methodName = InfinityClient.Methods.GET_INFRA_BY_ID
				.getValue();
		final MultivaluedMapImpl queryParams = this
				.getDefaultQueryParams(methodName);
		queryParams.add("serviceParameters", "[id]");
		queryParams.add("id", id.toString());

		final String jsonString = this.getJsonString(queryParams);

		try {
			return this.parser.parseGetInfrastructuresById(jsonString);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<InfinityValueID> searchInfrastructures() {
		final String methodName = InfinityClient.Methods.SEARCH_INFRASTRUCTURES
				.getValue();
		final MultivaluedMapImpl queryParams = this
				.getDefaultQueryParams(methodName);
		queryParams.add("serviceParameters", "%5btext,country,component%5d");

		final String jsonString = this.getJsonString(queryParams);

		try {
			return this.parser.parseSearchInfrastructuresResponse(jsonString);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<InfinityValueID> getTechnicalComponents() {
		final String methodName = InfinityClient.Methods.GET_TECHNICAL_COMPONENTS
				.getValue();
		final MultivaluedMapImpl queryParams = this
				.getDefaultQueryParams(methodName);
		final String jsonString = this.getJsonString(queryParams);
		try {
			return this.parser.parseGetTechnicalComponentsResponse(jsonString);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<InfinityArrayList> getComponentDetail(
			final String infrastructureId, final String componentId) {
		final String methodName = InfinityClient.Methods.GET_COMPONENT_DETAIL
				.getValue();
		final MultivaluedMapImpl queryParams = this
				.getDefaultQueryParams(methodName);

		// serviceParameters=[idInfra,idComponent]&idInfra=900&idComponent=242
		queryParams.add("serviceParameters", "%5bidInfra,idComponent%5d");

		if ((infrastructureId != null) && (infrastructureId.compareTo("") != 0)) {
			queryParams.add("idInfra", infrastructureId);
		}

		if ((componentId != null) && (componentId.compareTo("") != 0)) {
			queryParams.add("idComponent", componentId);
		}

		final String jsonString = this.getJsonString(queryParams);
		try {
			return this.parser.parseGetComponentDetailResponse(jsonString);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String getJsonString(final MultivaluedMapImpl queryParams) {
		final WebResource resource = this.path.queryParams(queryParams);
		final ClientResponse clientResponse = resource.accept(
				MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);

		final InputStream entityInputStream = clientResponse
				.getEntityInputStream();

		final InputStream inpuster = this.fixXIPIEncoding(entityInputStream);
		return this.convertStreamToString(inpuster);

	}

	private MultivaluedMapImpl getDefaultQueryParams(final String methodName) {
		final MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
		queryParams.add("serviceClassName",
				"com.liferay.infinity.service.InfrastructureServiceUtil");
		queryParams.add("serviceMethodName", methodName);
		return queryParams;
	}

}
