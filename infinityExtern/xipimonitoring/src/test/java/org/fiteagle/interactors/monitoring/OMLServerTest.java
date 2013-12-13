//package org.fiteagle.interactors.monitoring;
//
//import static org.junit.Assert.*;
//
//import java.net.URISyntaxException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.Date;
//import java.util.Iterator;
//
//import javax.xml.bind.DatatypeConverter;
//
//import junit.framework.Assert;
//
//import org.fiteagle.core.monitoring.StatusTable;
//import org.fiteagle.delivery.rest.monitoring.StatusPresenter;
//import org.fiteagle.delivery.rest.monitoring.TestbedStatus;
//import org.fiteagle.interactors.monitoring.client.OMLClientMock;
//import org.fiteagle.interactors.monitoring.server.OMLServer;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//public class OMLServerTest {
//	
////	private String omlStreamAsString = "protocol: 4\n" + 
////			"domain: FOKUS FUSECO Playground\n" + 
////			"start-time: 0\n" + 
////			"sender-id: fuseco.fokus.fraunhofer.de\n" + 
////			"app-name: fiteagle\n" + 
////			"schema: 0 _experiment_metadata subject:string key:string value:string\n" + 
////			"schema: 1 testComponent statusMessage:string up:double last_check:string\n" + 
////			"schema: 2 anotherTestComponent statusMessage:string up:double last_check:string\n" + 
////			"schema: 3 lastTestComponent statusMessage:string up:double last_check:string\n" + 
////			"content: text\n" + 
////			"0.908463954926	1	0	fine	1	2013-12-12T12:34:34.102734+02:00\n" + 
////			"0.908607959747	2	0	executing server update	0	2013-12-12T12:34:34.102734+02:00\n" + 
////			"0.908663988113	3	0	up and running	1	2013-12-12T12:34:34.102734+02:00";
//	
//	
//
//	// change this date in example stream text file and here to test correctly.
//	// This date will be too old to be accepted.
//	String dateInStreamAsString = "2013-12-12T12:34:34.102734+02:00";
//	String testbedName = "FOKUS FUSECO Playground";
//	OMLClientMock client;
//	private StatusPresenter statusPresenter;
//
//	@Before
//	public void setup() {
//		this.statusPresenter = new StatusPresenter();
//		this.client = new OMLClientMock(Utils.OML_SERVER_HOSTNAME,
//				Utils.OML_SERVER_PORT_NUMBER);
////		coffeeContainer = EasyMock.createMock(CoffeeContainer.class);
////		EasyMock.expect(waterContainer.getPortion(Portion.LARGE)).andReturn(true);
//	}
//	
//
//	@Test
//	public void testPushMonitoringData() throws ParseException {
//		statusPresenter.getStatus();
//		client.run();
//
//		Date dateInStream = parseStringToDate(dateInStreamAsString);
//
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		TestbedStatus data = statusPresenter.getTestBedStatusById(testbedName);
//		Assert.assertNotNull(data);
//		Assert.assertEquals(testbedName, data.getId());
//		Assert.assertNotNull(data.getComponents());
////		Assert.assertFalse(data.getComponents().isEmpty());
//		// Assert.assertEquals("partially", data.getStatus());
//
//		Collection<TestbedStatus> components = data.getComponents();
//		Assert.assertFalse(data.getComponents().isEmpty());
//
//
//		for (Iterator iterator = components.iterator(); iterator.hasNext();) {
//			TestbedStatus componentStatus = (TestbedStatus) iterator.next();
////			Assert.assertEquals(componentStatus.getLastCheck().getTime(),
////					dateInStream.getTime());
//			
//
//			System.out.println("server time: "+ componentStatus.getLastCheck().getTime()+" componentStatus time: "
//					+dateInStream.getTime());
//			System.out.println("COMPONENTID IS: " + componentStatus.getId()
//					+ " last checked: " + componentStatus.getLastCheck()
//					+ " status: " + componentStatus.getStatus()
//					+ " status message: " + componentStatus.getStatusMessage());
//		}
//
//	}
//	
////	@Test
////	public void testPushMonitoringDataWithLastCheckedInFuture() throws ParseException {
////		this.client = new OMLClientMock(Utils.OML_SERVER_HOSTNAME,
////				Utils.OML_SERVER_PORT_NUMBER, null);
////		statusPresenter.getStatus();
////		client.runWithStringInput(omlStreamAsString);
////
////		Date dateInStream = parseStringToDate(dateInStreamAsString);
////
////		try {
////			Thread.sleep(2000);
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
////
////		TestbedStatus data = statusPresenter.getTestBedStatusById(testbedName);
////		Assert.assertNotNull(data);
////		Assert.assertEquals(testbedName, data.getId());
////		Assert.assertNotNull(data.getComponents());
//////		Assert.assertFalse(data.getComponents().isEmpty());
////		// Assert.assertEquals("partially", data.getStatus());
////
////		Collection<TestbedStatus> components = data.getComponents();
////		Assert.assertFalse(data.getComponents().isEmpty());
////
////
////		for (Iterator iterator = components.iterator(); iterator.hasNext();) {
////			TestbedStatus componentStatus = (TestbedStatus) iterator.next();
////			Assert.assertEquals(componentStatus.getLastCheck().getTime(),
////					dateInStream.getTime());
////
////			System.out.println("COMPONENTID IS: " + componentStatus.getId()
////					+ " last checked: " + componentStatus.getLastCheck()
////					+ " status: " + componentStatus.getStatus()
////					+ " status message: " + componentStatus.getStatusMessage());
////		}
////
////	}
//	
//	
//
////	@Test
////	public void testParseStringToDate() throws ParseException {
////		// Date dateInStream = parseStringToDate("2013-12-12T12:34:34.102734");
////		Date dateInStreamWorking = parseStringToDate("2013-12-12T12:34:34.102734+00:00");
////		// 2013-12-12T12:34:34.102734+02:00
////		// System.out.println("original datra: "+dateInStream);
////		System.out.println("original datra like but working: "
////				+ dateInStreamWorking);
////
////		// Date now = new Date();
////		// SimpleDateFormat dateformatyyyyMMdd = new
////		// SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSz");
////		// String date_to_string = dateformatyyyyMMdd.format(now);
////		// System.out.println("request str: "+date_to_string);
////		// Date date = parseStringToDate(date_to_string);
////		// System.out.println(date);
////
////	}
//
//	private Date parseStringToDate(String dateString) throws ParseException {
//
//		Calendar response = DatatypeConverter.parseDate(dateString);
//		Date result = new Date(response.getTimeInMillis());
//		return result;
//		// result.set
//
//		// SimpleDateFormat simpleDate = new
//		// SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//
//		// StringBuilder dateStrBuilder = new StringBuilder(dateString);
//		// // dateStrBuilder.deleteCharAt(dateString.lastIndexOf(":"));
//		// Date response = simpleDate.parse(dateStrBuilder.toString());
//
//		// Date response = simpleDate.parse(dateString);
//		// System.out.println("server side time zone: "+response.getTimezoneOffset());
//		// return response;
//
//		// System.out.println("time zone: "+response.getTimezoneOffset());
//		//
//		// return response;
//
//		// SimpleDateFormat simpleDate = new SimpleDateFormat(
//		// "yyyy-MM-dd'T'HH:mm:ss.SSSSSSz");
//		//
//		// StringBuilder dateStrBuilder = new StringBuilder(dateString);
//		// dateStrBuilder.deleteCharAt(dateString.lastIndexOf(":"));
//		//
//		// return simpleDate.parse(dateStrBuilder.toString());
//
//	}
//
//}
