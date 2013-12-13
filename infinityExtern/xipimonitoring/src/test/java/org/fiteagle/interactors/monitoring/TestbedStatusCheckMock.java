package org.fiteagle.interactors.monitoring;

public class TestbedStatusCheckMock extends TestbedStatusCheck {
	
	private long now;
	
	
	@Override
	public long getNowInMilis() {
		return getNow();
	}
	
	
	

	public long getNow() {
		return now;
	}

	public void setNow(long now) {
		this.now = now;
	}

}
