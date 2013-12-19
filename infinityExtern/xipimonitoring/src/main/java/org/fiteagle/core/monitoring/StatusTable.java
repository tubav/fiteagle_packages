package org.fiteagle.core.monitoring;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StatusTable {

	public final static String UNDEFINED = "undefined";
	public final static String UP_AND_LAST_CHECKED_OLD = "upAndLastCheckedOld";
	public final static String UP = "up";
	public final static String PARTIALLY = "partially";
	public final static String DOWN = "down";

	private String id;
	private String status = null;
	private Date lastCheck = new Date();
	private String organization;
	private String xipiId;
	private String statusMessage;
	// private HashMap<String, StatusTable> components;
	private Map<String, StatusTable> components;

	// private String name;
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(final String status) {
		this.status = status;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}


	public Date getLastCheck() {
		return new Date(this.lastCheck.getTime());
	}

	public void setLastCheck(final Date lastCheck) {
		if (null != lastCheck) {
			this.lastCheck = new Date(lastCheck.getTime());
		}
	}

	public String getOrganization() {
		return this.organization;
	}

	public void setOrganization(final String organization) {
		this.organization = organization;
	}

	// public String getName() {
	// return name;
	// }
	// public void setName(String name) {
	// this.name = name;
	// }
	public String getXipiId() {
		return this.xipiId;
	}

	public void setXipiId(final String xipiId) {
		this.xipiId = xipiId;
	}

	public Collection<StatusTable> getComponents() {
		if (this.components == null) {
			this.components = new HashMap<String, StatusTable>();
		}
		return this.components.values();
	}

	public void setComponents(final Map<String, StatusTable> components) {
		this.components = components;
	}

	public void addComponent(final StatusTable component) {
		if (this.components == null) {
			this.components = new HashMap<String, StatusTable>();
		}

		this.components.put(component.getId(), component);
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	public void setStatusMessage(final String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public void removeComponent(final StatusTable component) {
		this.components.remove(component.getId());
	}

}
