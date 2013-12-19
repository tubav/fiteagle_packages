package org.fiteagle.delivery.rest.monitoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.fiteagle.core.monitoring.StatusTable;
import org.fiteagle.interactors.api.ResourceMonitoringBoundary;
import org.fiteagle.interactors.monitoring.MonitoringManager;

@Path("/v1/status/")
public class StatusPresenter {

	private final ResourceMonitoringBoundary monitor = new MonitoringManager();

	@GET
	@Path("")
	@Produces("application/json")
	public List<TestbedStatus> getStatus() {
		final ArrayList<TestbedStatus> status = new ArrayList<TestbedStatus>();

		final Collection<StatusTable> monitoringData = this.monitor
				.getMonitoringData();

		for (final Object element : monitoringData) {
			final StatusTable statusTable = (StatusTable) element;
			status.add(this.statusTable2Testbedstatus(statusTable));
		}

		return status;

	}

	@GET
	@Path("getById")
	@Produces("application/json")
	public TestbedStatus getTestBedStatusById(@QueryParam("id") final String id) {
		return this.statusTable2Testbedstatus(this.monitor
				.getMonitoringDataById(id));
	}

	private TestbedStatus statusTable2Testbedstatus(
			final StatusTable statusTable) {
		if (statusTable == null) {
			return null;
		}
		final TestbedStatus testbedStatus = new TestbedStatus();

		testbedStatus.setId(statusTable.getId());
		testbedStatus.setLastCheck(statusTable.getLastCheck());
		testbedStatus.setStatus(statusTable.getStatus());
		testbedStatus.setStatusMessage(statusTable.getStatusMessage());

		if (statusTable.getComponents() != null) {
			final Collection<StatusTable> components = statusTable
					.getComponents();
			for (final Object element : components) {
				final StatusTable statusTable2 = (StatusTable) element;
				testbedStatus.addComponent(this
						.statusTable2Testbedstatus(statusTable2));
			}
		}

		return testbedStatus;

	}

}
