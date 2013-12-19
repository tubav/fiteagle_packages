package org.fiteagle.interactors.monitoring;

import java.util.Collection;
import java.util.Date;

import org.fiteagle.core.monitoring.StatusTable;

public class TestbedStatusCheck {
	long timeForOldLastCheckedInMilis = Utils.timeForOldLastCheckedInMilis;
	long timeForTooOldNotAcceptableLastCheckedInMilis = Utils.timeForTooOldNotAcceptableLastCheckedInMilis;
	long acceptableTimeDifferenceForNowInMilis = 60000L;

	public boolean isLastCheckedOld(final Date lastCheckedDate) {
		final long nowInMilis = this.getNowInMilis();
		final long lastCheckedDateInMilis = lastCheckedDate.getTime();
		return (nowInMilis - lastCheckedDateInMilis) > this.timeForOldLastCheckedInMilis;
	}

	public long getNowInMilis() {
		return new Date().getTime();
	}

	public boolean isLastCheckedTooOld(final Date lastCheckedDate) {
		final long nowInMilis = this.getNowInMilis();
		final long lastCheckedDateInMilis = lastCheckedDate.getTime();
		return (nowInMilis - lastCheckedDateInMilis) > this.timeForTooOldNotAcceptableLastCheckedInMilis;
	}

	public StatusTable updateComponentsOfTestbed(final StatusTable testbed) {

		final Collection<StatusTable> components = testbed.getComponents();

		if ((components == null) || components.isEmpty()) {
			return testbed;
		}

		for (final Object element : components) {
			final StatusTable component = (StatusTable) element;
			if (component.getLastCheck() != null) {
				if (this.isLastCheckedTooOld(component.getLastCheck())) {
					testbed.removeComponent(component);
					continue;
				}
				if ((component.getStatus().compareTo(StatusTable.UP) == 0)
						&& this.isLastCheckedOld(component.getLastCheck())) {
					component.setStatus(StatusTable.UP_AND_LAST_CHECKED_OLD);
				}
			}
			testbed.addComponent(component);

		}

		return testbed;

	}

	public StatusTable updateStatusTableState(final StatusTable statusTable) {

		Date lastChecked = new Date();
		statusTable.setStatus(StatusTable.UNDEFINED);

		final Collection<StatusTable> components = statusTable.getComponents();

		for (final Object element : components) {
			final StatusTable statusTableComponent = (StatusTable) element;

			if (statusTableComponent.getStatus().compareTo(StatusTable.UP) == 0) {
				if (statusTable.getStatus().compareTo(StatusTable.DOWN) == 0) {
					statusTable.setStatus(StatusTable.PARTIALLY);
				}
				if (statusTable.getStatus().compareTo(StatusTable.UNDEFINED) == 0) {
					statusTable.setStatus(StatusTable.UP);
				}
			}

			if (statusTableComponent.getStatus().compareTo(
					StatusTable.UP_AND_LAST_CHECKED_OLD) == 0) {
				if (statusTable.getStatus().compareTo(StatusTable.DOWN) == 0) {
					statusTable.setStatus(StatusTable.PARTIALLY);
				}
				if (statusTable.getStatus().compareTo(StatusTable.UNDEFINED) == 0) {
					statusTable.setStatus(StatusTable.UP_AND_LAST_CHECKED_OLD);
				}
				if (statusTable.getStatus().compareTo(StatusTable.UP) == 0) {
					statusTable.setStatus(StatusTable.UP_AND_LAST_CHECKED_OLD);
				}
			}

			if (statusTableComponent.getStatus().compareTo(StatusTable.DOWN) == 0) {
				if ((statusTable.getStatus().compareTo(StatusTable.UNDEFINED) == 0)
						|| (statusTable.getStatus().compareTo(StatusTable.DOWN) == 0)) {
					statusTable.setStatus(StatusTable.DOWN);
				} else {
					statusTable.setStatus(StatusTable.PARTIALLY);
				}
			}

			if (statusTableComponent.getStatus().compareTo(
					StatusTable.UNDEFINED) == 0) {
				if (statusTable.getStatus().compareTo(StatusTable.UNDEFINED) == 0) {
					statusTable.setStatus(StatusTable.UNDEFINED);
				} else if (statusTable.getStatus().compareTo(StatusTable.DOWN) == 0) {
					statusTable.setStatus(StatusTable.DOWN);
				} else {
					statusTable.setStatus(StatusTable.PARTIALLY);
				}
			}

			if (statusTableComponent.getLastCheck().before(lastChecked)) {
				lastChecked = statusTableComponent.getLastCheck();
			}
		}

		statusTable.setLastCheck(lastChecked);

		return statusTable;
	}

	public boolean isLastCheckedInFuture(final Date lastCheckedDate) {
		final long nowInMilis = this.getNowInMilis();
		final long lastCheckedDateInMilis = lastCheckedDate.getTime();
		return (lastCheckedDateInMilis - nowInMilis) > this.acceptableTimeDifferenceForNowInMilis;
	}
}
