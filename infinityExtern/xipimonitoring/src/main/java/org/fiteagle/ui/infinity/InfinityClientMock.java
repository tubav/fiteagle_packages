package org.fiteagle.ui.infinity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.fiteagle.ui.infinity.model.InfinityArrayList;
import org.fiteagle.ui.infinity.model.InfinityInfrastructure;
import org.fiteagle.ui.infinity.model.InfinityValueID;

public class InfinityClientMock extends InfinityClient {

	@Override
	public InfinityInfrastructure getInfrastructuresById(final Number i) {
		String input;
		input = this.getMockedInput("/getInfrastructuresByIdResponse.json");

		InfinityInfrastructure result = null;
		try {
			result = this.parser.parseGetInfrastructuresById(input);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	@Override
	public List<InfinityValueID> searchInfrastructures() {
		String input;
		input = this.getMockedInput("/searchInfrastructuresResponse.json");

		List<InfinityValueID> result = null;
		try {
			result = this.parser.parseSearchInfrastructuresResponse(input);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	private String getMockedInput(final String path) {
		final InputStream in = this.getClass().getResourceAsStream(path);
		final InputStream fixedIn = this.fixXIPIEncoding(in);
		final String result = this.convertStreamToString(fixedIn);

		try {
			in.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		try {
			fixedIn.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public List<InfinityValueID> getTechnicalComponents() {
		return null;
	}

	@Override
	public List<InfinityArrayList> getComponentDetail(
			final String infrastructureId, final String componentId) {
		return null;
	}

}
