package org.fiteagle.interactors.monitoring;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.xml.bind.DatatypeConverter;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.fiteagle.delivery.rest.monitoring.StatusPresenter;
import org.fiteagle.delivery.rest.monitoring.TestbedStatus;
import org.fiteagle.interactors.monitoring.client.OMLClientMock;
import org.fiteagle.interactors.monitoring.server.ClientHandler;
import org.junit.Before;
import org.junit.Test;

public class ClientHandlerTest {

	String testbedName = "FOKUS FUSECO Playground";

	String firstComponentName = "testComponent";
	String secondComponentName = "anotherTestComponent";
	String thirdComponentName = "lastTestComponent";

	String firstComponentLastChecked = "2013-12-12T12:34:34.102734+02:00";
	String secondComponentLastChecked = "2013-12-12T12:34:34.102734+02:00";
	String thirdComponentLastChecked = "2013-12-12T12:34:34.102734+02:00";

	String nowString = "2013-12-12T12:35:34.102734+02:00";

	private StatusPresenter statusPresenter;
	private ClientHandler clientHandler;
	private TestbedStatusCheckMock testbedStatusCheck;
	
	//All tests => check the last checked of testbed itself also with minimum date of test case

	@Before
	public void setUp() throws Exception {
		this.statusPresenter = new StatusPresenter();
		statusPresenter.getStatus();
		this.testbedStatusCheck = new TestbedStatusCheckMock();
		testbedStatusCheck.setNow(parseStringToDate(nowString).getTime());
	}

	@Test
	public void testRun() throws ParseException {
		pushStream(this.getClass().getResourceAsStream("/pushOMLStringAllUp.txt"));
		Date dateInStream = parseStringToDate(firstComponentLastChecked);
		
		TestbedStatus data = statusPresenter.getTestBedStatusById(testbedName);
		Assert.assertNotNull(data);
		Assert.assertEquals(testbedName, data.getId());
		Assert.assertNotNull(data.getComponents());
		// Assert.assertFalse(data.getComponents().isEmpty());
		// Assert.assertEquals("partially", data.getStatus());
		Collection<TestbedStatus> components = data.getComponents();
		Assert.assertFalse(data.getComponents().isEmpty());
		for (Iterator iterator = components.iterator(); iterator.hasNext();) {
			TestbedStatus componentStatus = (TestbedStatus) iterator.next();
			Assert.assertEquals(componentStatus.getLastCheck().getTime(),
					dateInStream.getTime());
		}
	}
	
	@Test
	public void testRunAllUp() throws ParseException, FileNotFoundException {
		pushStream(this.getClass().getResourceAsStream("/pushOMLStringAllUp.txt"));
		Date dateInStream = parseStringToDate(firstComponentLastChecked);

		TestbedStatus data = statusPresenter.getTestBedStatusById(testbedName);
		Assert.assertNotNull(data);
		Assert.assertEquals(testbedName, data.getId());
		Assert.assertNotNull(data.getComponents());
		// Assert.assertFalse(data.getComponents().isEmpty());
		System.out.println("DATASTATUSC; "+data.getStatus());
//		Assert.assertEquals("up", data.getStatus());
		Collection<TestbedStatus> components = data.getComponents();
		System.out.println("TESTBED LAST CEHCEKECD: "+data.getLastCheck());
		Assert.assertFalse(data.getComponents().isEmpty());
		for (Iterator iterator = components.iterator(); iterator.hasNext();) {
			TestbedStatus componentStatus = (TestbedStatus) iterator.next();
			System.out.println("componentStatus time: "+ componentStatus.getLastCheck().getTime()+" server time: "
					+dateInStream.getTime());
			System.out.println("COMPONENTID IS: " + componentStatus.getId()
					+ " last checked: " + componentStatus.getLastCheck()
					+ " status: " + componentStatus.getStatus()
					+ " status message: " + componentStatus.getStatusMessage());
			System.out.println("NOW IS: "+new Date());
			Assert.assertEquals(componentStatus.getLastCheck().getTime(),
					dateInStream.getTime());
		}
	}

	private void pushStream(InputStream inputStreamOfText)
			throws ParseException {
		this.clientHandler = new ClientHandler(new BufferedReader(new InputStreamReader(inputStreamOfText)));
		clientHandler.setTestbedStatusCheck(testbedStatusCheck);
		clientHandler.run();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	

	private Date parseStringToDate(String dateString) throws ParseException {
		Calendar response = DatatypeConverter.parseDate(dateString);
		Date result = new Date(response.getTimeInMillis());
		return result;
	}

}
