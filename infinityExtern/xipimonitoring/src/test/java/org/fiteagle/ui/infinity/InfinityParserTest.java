package org.fiteagle.ui.infinity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;

import org.fiteagle.ui.infinity.model.InfinityArrayList;
import org.fiteagle.ui.infinity.model.InfinityInfrastructure;
import org.fiteagle.ui.infinity.model.InfinityValueID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InfinityParserTest {

	private InfinityParser parser;

	@Before
	public void setup() {
		this.parser = new InfinityParser();
	}

	@Test
	public void testGetTechnicalComponentsParser() throws IOException {
		final String input = this
				.getMockedInput("/getTechnicalComponentsResponse.json");
		final List<InfinityValueID> result = this.parser
				.parseGetTechnicalComponentsResponse(input);
		Assert.assertFalse(result.isEmpty());
	}

	@Test
	public void testGetInfrastructuresByIdParser() throws IOException {
		final String input = this
				.getMockedInput("/getInfrastructuresByIdResponse.json");
		final InfinityInfrastructure result = this.parser
				.parseGetInfrastructuresById(input);
		Assert.assertNotNull(result);
	}

	@Test
	public void testWritingGetInfrastructuresByIdResponse() throws IOException {
		final String input = this
				.getMockedInput("/getInfrastructuresByIdResponse.json");
		final InfinityInfrastructure result = this.parser
				.parseGetInfrastructuresById(input);
		this.parser.write(new NullOutputStream(), result);
	}

	@Test
	public void testGetComponentDetailResponseParser() throws IOException {
		final String input = this
				.getMockedInput("/getComponentDetailResponse.json");
		final List<InfinityArrayList> result = this.parser
				.parseGetComponentDetailResponse(input);
		Assert.assertFalse(result.isEmpty());
	}

	@Test
	public void testSearchInfrastructuresResponseParser() throws IOException {
		final String input = this
				.getMockedInput("/searchInfrastructuresResponse.json");
		final List<InfinityValueID> result = this.parser
				.parseSearchInfrastructuresResponse(input);
		Assert.assertFalse(result.isEmpty());
	}

	private String getMockedInput(final String path) {
		final InputStream in = this.getClass().getResourceAsStream(path);
		return InfinityParserTest.convertStreamToString(in);
	}

	private static String convertStreamToString(final InputStream is) {
		@SuppressWarnings("resource")
		final Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	private class NullOutputStream extends OutputStream {
		@Override
		public void write(final int b) throws IOException {
		}
	}
}
