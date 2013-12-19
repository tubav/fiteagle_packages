package org.fiteagle.interactors.monitoring;

public final class Utils {
	
	private Utils() {
	}
	
	static String xipiURIString = "http://www.xipi.eu";
	private static String pathPortlet = "/InfinityServices-portlet/json";
	private static int omlServerPortNumber = 3434;
	private static String omlServerHostName = "localhost";
	static long timeForOldLastCheckedInMilis = 60000L; // default 1 minute
	static long timeForTooOldNotAcceptableLastCheckedInMilis = 172800000L; // default
	// 2
	// days
	static int scheduledStatusCheckPeriod = 5;

	public static String getOmlServerHostName() {
		return Utils.omlServerHostName;
	}

	public static int getOmlServerPortNumber() {
		return Utils.omlServerPortNumber;
	}

	public static String getPathPortlet() {
		return Utils.pathPortlet;
	}

	public static void setOmlServerHostName(final String omlServerHostName) {
		Utils.omlServerHostName = omlServerHostName;
	}

	public static void setOmlServerPortNumber(final int omlServerPortNumber) {
		Utils.omlServerPortNumber = omlServerPortNumber;
	}

	public static void setPathPortlet(final String pathPortlet) {
		Utils.pathPortlet = pathPortlet;
	}

}
