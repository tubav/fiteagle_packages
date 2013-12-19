package org.fiteagle.ui.infinity.model;

public class InfinityValueID {
	private String id;
	private String value;
	private String other;
	private String name;
	private String javaClass;

	public String getJavaClass() {
		return this.javaClass;
	}

	public void setJavaClass(final String javaClass) {
		this.javaClass = javaClass;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String getOther() {
		return this.other;
	}

	public void setOther(final String other) {
		this.other = other;
	}
}
