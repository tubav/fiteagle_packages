package org.fiteagle.ui.infinity.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InfinityArrayList {

	private String javaClass;
	private java.util.List<InfinityValueID> list = new ArrayList<InfinityValueID>();
	private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public String getJavaClass() {
		return this.javaClass;
	}

	public void setJavaClass(final String javaClass) {
		this.javaClass = javaClass;
	}

	public java.util.List<InfinityValueID> getList() {
		return this.list;
	}

	public void setList(final java.util.List<InfinityValueID> list) {
		this.list = list;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperties(final String name, final Object value) {
		this.additionalProperties.put(name, value);
	}

}
