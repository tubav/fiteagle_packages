package org.fiteagle.delivery.rest.monitoring;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestbedStatus {

	private String id;
	private String status;
	private Date lastCheck = new Date();
	private String statusMessage;
	private List<TestbedStatus> components;

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public Date getLastCheck() {
		return new Date(this.lastCheck.getTime());
	}

	public void setLastCheck(final Date lastCheck) {
		if (null != lastCheck) {
			this.lastCheck = new Date(lastCheck.getTime());
		}
	}

	public List<TestbedStatus> getComponents() {
		return this.components;
	}

	public void setComponents(final List<TestbedStatus> components) {
		this.components = components;
	}

	public void addComponent(final TestbedStatus component) {
		if (this.components == null) {
			this.components = new ArrayList<TestbedStatus>();
		}

		this.components.add(component);
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	public void setStatusMessage(final String message) {
		this.statusMessage = message;
	}
}
