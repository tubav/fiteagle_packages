package org.fiteagle.interactors.monitoring;

public class Utils {
	public static String XIPI_URI_STRING="http://www.xipi.eu";
	public static String PATH_PORTLET = "/InfinityServices-portlet/json";
	public static int OML_SERVER_PORT_NUMBER=3434;
	public static String OML_SERVER_HOSTNAME = "localhost";
	public static long timeForOldLastCheckedInMilis = 60000L; //default 1 minute
	public static long timeForTooOldNotAcceptableLastCheckedInMilis = 172800000L; //default 2 days
	public static int scheduledStatusCheckPeriod = 5;
	
}
