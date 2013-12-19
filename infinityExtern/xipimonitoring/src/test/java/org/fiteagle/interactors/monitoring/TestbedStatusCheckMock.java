package org.fiteagle.interactors.monitoring;

public class TestbedStatusCheckMock extends TestbedStatusCheck {

	private long now;

	@Override
	public long getNowInMilis() {
		return this.getNow();
	}

	public long getNow() {
		return this.now;
	}

	public void setNow(final long now) {
		this.now = now;
	}

}
