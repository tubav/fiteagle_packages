package org.fiteagle.ui.infinity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import org.fiteagle.ui.infinity.model.InfinityArrayList;
import org.fiteagle.ui.infinity.model.InfinityInfrastructure;
import org.fiteagle.ui.infinity.model.InfinityValueID;

public abstract class InfinityClient {

	static int i = 0;

	protected InfinityParser parser;

	protected enum Methods {
		GET_INFRA_BY_ID("getInfrastructuresById"), GET_COMPONENT_BY_ID(
				"getComponentById"), SEARCH_INFRASTRUCTURES(
				"searchInfrastructures"), GET_TECHNICAL_COMPONENTS(
				"getTechnicalComponents"), GET_COMPONENT_DETAIL(
				"getComponentDetail");
		private String value;

		private Methods(final String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public InfinityClient() {
		this.parser = new InfinityParser();
	}

	public abstract InfinityInfrastructure getInfrastructuresById(Number i);

	public abstract List<InfinityValueID> searchInfrastructures();

	public abstract List<InfinityValueID> getTechnicalComponents();

	public abstract List<InfinityArrayList> getComponentDetail(
			String infrastructureId, String componentId);

	public InputStream fixXIPIEncoding(final InputStream in) {

		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = in.read(buffer)) > -1) {
				out.write(buffer, 0, len);
			}
		} catch (final IOException e2) {
			throw new RuntimeException(e2);
		}
		try {
			out.flush();
		} catch (final IOException e1) {
			throw new RuntimeException(e1);
		}

		byte[] utf8 = null;
		try {
			utf8 = new String(out.toByteArray(), "cp1252").getBytes("UTF-8");
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		return new ByteArrayInputStream(utf8);
	}

	public String convertStreamToString(final InputStream is) {
		@SuppressWarnings("resource")
		final Scanner s = new Scanner(is, StandardCharsets.UTF_8.toString())
				.useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
