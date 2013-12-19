package org.fiteagle.ui.infinity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.fiteagle.ui.infinity.model.InfinityArrayList;
import org.fiteagle.ui.infinity.model.InfinityInfrastructure;
import org.fiteagle.ui.infinity.model.InfinityValueID;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InfinityParser {

	private final JsonFactory factory = new JsonFactory();
	private final ObjectMapper mapper = new ObjectMapper(this.factory);

	public List<InfinityValueID> parseGetTechnicalComponentsResponse(
			final String string) throws JsonParseException,
			JsonMappingException, IOException {
		final ArrayList<InfinityValueID> result = this.mapper.readValue(string,
				new TypeReference<java.util.List<InfinityValueID>>() {
				});
		return result;
	}

	public InfinityInfrastructure parseGetInfrastructuresById(final String input)
			throws JsonParseException, JsonMappingException, IOException {
		final InfinityInfrastructure result = this.mapper.readValue(input,
				InfinityInfrastructure.class);
		return result;
	}

	public List<InfinityArrayList> parseGetComponentDetailResponse(
			final String input) throws JsonParseException,
			JsonMappingException, IOException {
		final ArrayList<InfinityArrayList> result = this.mapper.readValue(
				input, new TypeReference<java.util.List<InfinityArrayList>>() {
				});
		return result;
	}

	public List<InfinityValueID> parseSearchInfrastructuresResponse(
			final String input) throws JsonParseException,
			JsonMappingException, IOException {
		return this.parseGetTechnicalComponentsResponse(input);
	}

	public void write(final OutputStream stream,
			final InfinityInfrastructure input) throws JsonGenerationException,
			JsonMappingException, IOException {
		this.mapper.writeValue(stream, input);
	}
}
