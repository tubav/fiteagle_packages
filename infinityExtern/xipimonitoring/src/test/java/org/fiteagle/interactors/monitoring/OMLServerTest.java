package org.fiteagle.interactors.monitoring;

import static org.junit.Assert.*;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.xml.bind.DatatypeConverter;

import junit.framework.Assert;

import org.fiteagle.core.monitoring.StatusTable;
import org.fiteagle.delivery.rest.monitoring.StatusPresenter;
import org.fiteagle.delivery.rest.monitoring.TestbedStatus;
import org.fiteagle.interactors.monitoring.client.OMLClientMock;
import org.fiteagle.interactors.monitoring.server.OMLServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OMLServerTest {

	// change this date in example stream text file and here to test correctly.
	// This date will be too old to be accepted.
	String dateInStreamAsString = "2013-12-12T12:34:34.102734+02:00";
	String testbedName = "FOKUS FUSECO Playground";
	OMLClientMock client;
	private StatusPresenter statusPresenter;

	@Before
	public void setup() {
		this.statusPresenter = new StatusPresenter();
	}

	@Test
	public void testPushMonitoringData() throws ParseException {
		this.client = new OMLClientMock(Utils.OML_SERVER_HOSTNAME,
				Utils.OML_SERVER_PORT_NUMBER, null);
		statusPresenter.getStatus();
		client.run();

		Date dateInStream = parseStringToDate(dateInStreamAsString);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		TestbedStatus data = statusPresenter.getTestBedStatusById(testbedName);
		Assert.assertNotNull(data);
		Assert.assertEquals(testbedName, data.getId());
		Assert.assertNotNull(data.getComponents());
//		Assert.assertFalse(data.getComponents().isEmpty());
		// Assert.assertEquals("partially", data.getStatus());

		Collection<TestbedStatus> components = data.getComponents();
		Assert.assertFalse(data.getComponents().isEmpty());


		for (Iterator iterator = components.iterator(); iterator.hasNext();) {
			TestbedStatus componentStatus = (TestbedStatus) iterator.next();
			Assert.assertEquals(componentStatus.getLastCheck().getTime(),
					dateInStream.getTime());

//			System.out.println("COMPONENTID IS: " + componentStatus.getId()
//					+ " last checked: " + componentStatus.getLastCheck()
//					+ " status: " + componentStatus.getStatus()
//					+ " status message: " + componentStatus.getStatusMessage());
		}

	}

//	@Test
//	public void testParseStringToDate() throws ParseException {
//		// Date dateInStream = parseStringToDate("2013-12-12T12:34:34.102734");
//		Date dateInStreamWorking = parseStringToDate("2013-12-12T12:34:34.102734+00:00");
//		// 2013-12-12T12:34:34.102734+02:00
//		// System.out.println("original datra: "+dateInStream);
//		System.out.println("original datra like but working: "
//				+ dateInStreamWorking);
//
//		// Date now = new Date();
//		// SimpleDateFormat dateformatyyyyMMdd = new
//		// SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSz");
//		// String date_to_string = dateformatyyyyMMdd.format(now);
//		// System.out.println("request str: "+date_to_string);
//		// Date date = parseStringToDate(date_to_string);
//		// System.out.println(date);
//
//	}

	private Date parseStringToDate(String dateString) throws ParseException {

		Calendar response = DatatypeConverter.parseDate(dateString);
		Date result = new Date(response.getTimeInMillis());
		return result;
		// result.set

		// SimpleDateFormat simpleDate = new
		// SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		// StringBuilder dateStrBuilder = new StringBuilder(dateString);
		// // dateStrBuilder.deleteCharAt(dateString.lastIndexOf(":"));
		// Date response = simpleDate.parse(dateStrBuilder.toString());

		// Date response = simpleDate.parse(dateString);
		// System.out.println("server side time zone: "+response.getTimezoneOffset());
		// return response;

		// System.out.println("time zone: "+response.getTimezoneOffset());
		//
		// return response;

		// SimpleDateFormat simpleDate = new SimpleDateFormat(
		// "yyyy-MM-dd'T'HH:mm:ss.SSSSSSz");
		//
		// StringBuilder dateStrBuilder = new StringBuilder(dateString);
		// dateStrBuilder.deleteCharAt(dateString.lastIndexOf(":"));
		//
		// return simpleDate.parse(dateStrBuilder.toString());

	}

}
