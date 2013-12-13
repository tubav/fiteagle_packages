package org.fiteagle.interactors.monitoring;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.fiteagle.core.monitoring.StatusTable;

public class TestbedStatusCheck {
	long timeForOldLastCheckedInMilis = Utils.timeForOldLastCheckedInMilis;
	long timeForTooOldNotAcceptableLastCheckedInMilis = Utils.timeForTooOldNotAcceptableLastCheckedInMilis;
	
	public boolean isLastCheckedOld(Date lastCheckedDate) {
		long nowInMilis = getNowInMilis();
		long lastCheckedDateInMilis = lastCheckedDate.getTime();
		return (nowInMilis - lastCheckedDateInMilis) > timeForOldLastCheckedInMilis;
	}

	public long getNowInMilis() {
		return new Date().getTime();
	}
	
	public boolean isLastCheckedTooOld(Date lastCheckedDate) {
		long nowInMilis = getNowInMilis();
		long lastCheckedDateInMilis = lastCheckedDate.getTime();
		return (nowInMilis - lastCheckedDateInMilis) > timeForTooOldNotAcceptableLastCheckedInMilis;
	}
	
	public StatusTable updateComponentsOfTestbed(StatusTable testbed){
		
		Collection<StatusTable> components = testbed.getComponents();
		
		if(components == null || components.size() == 0) return testbed;
		
		for (Iterator iterator = components.iterator(); iterator.hasNext();) {
			StatusTable component= (StatusTable) iterator.next();
			if(component.getLastCheck() != null){
				if(isLastCheckedTooOld(component.getLastCheck())){
					testbed.removeComponent(component);
					continue;
				}
				if (component.getStatus().compareTo(StatusTable.UP)==0 && isLastCheckedOld(component.getLastCheck())) {
					component.setStatus(StatusTable.UP_AND_LAST_CHECKED_OLD);
				}
			}
			testbed.addComponent(component);
			
		}
		
		return testbed;
		
	}
	
	public StatusTable updateStatusTableState(StatusTable statusTable) {

		Date lastChecked = new Date();
		statusTable.setStatus(StatusTable.UNDEFINED);

		Collection<StatusTable> components = statusTable.getComponents();

		for (Iterator iterator = components.iterator(); iterator.hasNext();) {
			StatusTable statusTableComponent = (StatusTable) iterator.next();

			if (statusTableComponent.getStatus().compareTo(StatusTable.UP) == 0) {
				if (statusTable.getStatus().compareTo(StatusTable.DOWN) == 0) {
					statusTable.setStatus(StatusTable.PARTIALLY);
				}
				if (statusTable.getStatus().compareTo(StatusTable.UNDEFINED) == 0)
					statusTable.setStatus(StatusTable.UP);
			}

			if (statusTableComponent.getStatus().compareTo(
					StatusTable.UP_AND_LAST_CHECKED_OLD) == 0) {
				if (statusTable.getStatus().compareTo(StatusTable.DOWN) == 0) {
					statusTable.setStatus(StatusTable.PARTIALLY);
				}
				if (statusTable.getStatus().compareTo(StatusTable.UNDEFINED) == 0)
					statusTable.setStatus(StatusTable.UP_AND_LAST_CHECKED_OLD);
				if (statusTable.getStatus().compareTo(StatusTable.UP) == 0)
					statusTable.setStatus(StatusTable.UP_AND_LAST_CHECKED_OLD);
			}

			if (statusTableComponent.getStatus().compareTo(StatusTable.DOWN) == 0) {
				if (statusTable.getStatus().compareTo(StatusTable.UNDEFINED) == 0
						|| statusTable.getStatus().compareTo(StatusTable.DOWN) == 0)
					statusTable.setStatus(StatusTable.DOWN);
				else
					statusTable.setStatus(StatusTable.PARTIALLY);
			}

			if (statusTableComponent.getStatus().compareTo(
					StatusTable.UNDEFINED) == 0) {
				if (statusTable.getStatus().compareTo(StatusTable.UNDEFINED) == 0) {
					statusTable.setStatus(StatusTable.UNDEFINED);
				} else if (statusTable.getStatus().compareTo(StatusTable.DOWN) == 0) {
					statusTable.setStatus(StatusTable.DOWN);
				} else
					statusTable.setStatus(StatusTable.PARTIALLY);
			}

			if (statusTableComponent.getLastCheck().before(lastChecked))
				lastChecked = statusTableComponent.getLastCheck();
		}

		statusTable.setLastCheck(lastChecked);

		return statusTable;
	}
}
