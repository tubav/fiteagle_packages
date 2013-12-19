package org.fiteagle.interactors.monitoring;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import junit.framework.Assert;

import org.fiteagle.delivery.rest.monitoring.StatusPresenter;
import org.fiteagle.delivery.rest.monitoring.TestbedStatus;
import org.fiteagle.interactors.monitoring.server.ClientHandler;
import org.junit.After;
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

	String firstComponentStatusMessage = "fine";
	String secondComponentStatusMessage = "executing server update";
	String thirdComponentStatusMessage = "up and running";

	String nowString = "2013-12-12T12:35:34.102734+02:00";

	private StatusPresenter statusPresenter;
	private ClientHandler clientHandler;
	private TestbedStatusCheckMock testbedStatusCheck;

	@Before
	public void setUp() throws Exception {
		this.statusPresenter = new StatusPresenter();
		this.statusPresenter.getStatus();
		this.testbedStatusCheck = new TestbedStatusCheckMock();
		this.testbedStatusCheck.setNow(this.parseStringToDate(this.nowString)
				.getTime());
	}

	@After
	public void tearDown() {
		new MonitoringManager().reset();
	}

	@Test
	public void testRunOneComponentDown() throws ParseException {
		this.pushStream(this.getClass().getResourceAsStream(
				"/pushOMLString.txt"));
		final Date dateInStream = this
				.parseStringToDate(this.firstComponentLastChecked);

		final TestbedStatus data = this.statusPresenter
				.getTestBedStatusById(this.testbedName);
		Assert.assertNotNull(data);
		Assert.assertEquals(this.testbedName, data.getId());
		Assert.assertNotNull(data.getComponents());
		Assert.assertFalse(data.getComponents().isEmpty());

		Assert.assertEquals("partially", data.getStatus());

		final Collection<TestbedStatus> components = data.getComponents();
		for (final Object element : components) {
			final TestbedStatus componentStatus = (TestbedStatus) element;
			Assert.assertEquals(componentStatus.getLastCheck().getTime(),
					dateInStream.getTime());

			if (componentStatus.getId().compareTo(this.firstComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.firstComponentStatusMessage);
			}
			if (componentStatus.getId().compareTo(this.secondComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.secondComponentStatusMessage);
			}
			if (componentStatus.getId().compareTo(this.thirdComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.thirdComponentStatusMessage);
			}
		}
	}

	@Test
	public void testRunAllUp() throws ParseException, FileNotFoundException {
		this.pushStream(this.getClass().getResourceAsStream(
				"/pushOMLStringAllUp.txt"));
		final Date dateInStream = this
				.parseStringToDate(this.firstComponentLastChecked);

		final TestbedStatus data = this.statusPresenter
				.getTestBedStatusById(this.testbedName);
		Assert.assertNotNull(data);
		Assert.assertEquals(this.testbedName, data.getId());
		Assert.assertNotNull(data.getComponents());
		Assert.assertFalse(data.getComponents().isEmpty());

		Assert.assertEquals("up", data.getStatus());

		final Collection<TestbedStatus> components = data.getComponents();
		for (final Object element : components) {
			final TestbedStatus componentStatus = (TestbedStatus) element;

			if (componentStatus.getId().compareTo(this.firstComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.firstComponentStatusMessage);
			}
			if (componentStatus.getId().compareTo(this.secondComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.secondComponentStatusMessage);
			}
			if (componentStatus.getId().compareTo(this.thirdComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.thirdComponentStatusMessage);
			}

			Assert.assertEquals(componentStatus.getLastCheck().getTime(),
					dateInStream.getTime());
		}
	}

	@Test
	public void testRunDown() throws ParseException, FileNotFoundException {
		this.pushStream(this.getClass().getResourceAsStream(
				"/pushOMLStringDown.txt"));
		final Date dateInStream = this
				.parseStringToDate(this.firstComponentLastChecked);

		final TestbedStatus data = this.statusPresenter
				.getTestBedStatusById(this.testbedName);
		Assert.assertNotNull(data);
		Assert.assertEquals(this.testbedName, data.getId());
		Assert.assertNotNull(data.getComponents());
		Assert.assertFalse(data.getComponents().isEmpty());

		Assert.assertEquals("down", data.getStatus());

		final Collection<TestbedStatus> components = data.getComponents();
		Assert.assertFalse(data.getComponents().isEmpty());
		for (final Object element : components) {
			final TestbedStatus componentStatus = (TestbedStatus) element;

			if (componentStatus.getId().compareTo(this.firstComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.firstComponentStatusMessage);
			}
			if (componentStatus.getId().compareTo(this.secondComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.secondComponentStatusMessage);
			}
			if (componentStatus.getId().compareTo(this.thirdComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.thirdComponentStatusMessage);
			}

			Assert.assertEquals(componentStatus.getLastCheck().getTime(),
					dateInStream.getTime());
		}
	}

	@Test
	public void testRunWithTooOldComponent() throws ParseException,
			FileNotFoundException {
		this.pushStream(this.getClass().getResourceAsStream(
				"/pushOMLStringTooOldComponent.txt"));
		final Date dateInStream = this
				.parseStringToDate(this.firstComponentLastChecked);

		final TestbedStatus data = this.statusPresenter
				.getTestBedStatusById(this.testbedName);
		Assert.assertNotNull(data);
		Assert.assertEquals(this.testbedName, data.getId());
		Assert.assertNotNull(data.getComponents());
		Assert.assertFalse(data.getComponents().isEmpty());

		Assert.assertEquals("up", data.getStatus());

		final Collection<TestbedStatus> components = data.getComponents();
		Assert.assertFalse(data.getComponents().isEmpty());
		for (final Object element : components) {
			final TestbedStatus componentStatus = (TestbedStatus) element;
			// second component is too old, should not be listed

			Assert.assertFalse(componentStatus.getId().compareTo(
					this.secondComponentName) == 0);

			if (componentStatus.getId().compareTo(this.firstComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.firstComponentStatusMessage);
			}
			if (componentStatus.getId().compareTo(this.thirdComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.thirdComponentStatusMessage);
			}

			Assert.assertEquals(componentStatus.getLastCheck().getTime(),
					dateInStream.getTime());
		}
	}

	@Test
	public void testRunWithOldComponent() throws ParseException,
			FileNotFoundException {
		this.pushStream(this.getClass().getResourceAsStream(
				"/pushOMLStringOldComponent.txt"));

		this.firstComponentLastChecked = "2013-12-12T12:30:34.102734+02:00";
		final Date dateInStream = this
				.parseStringToDate(this.secondComponentLastChecked);
		final Date dateFirstComponentInStream = this
				.parseStringToDate(this.firstComponentLastChecked);

		final TestbedStatus data = this.statusPresenter
				.getTestBedStatusById(this.testbedName);
		Assert.assertNotNull(data);
		Assert.assertEquals(this.testbedName, data.getId());
		Assert.assertNotNull(data.getComponents());
		Assert.assertFalse(data.getComponents().isEmpty());

		Assert.assertEquals("upAndLastCheckedOld", data.getStatus());

		final Collection<TestbedStatus> components = data.getComponents();
		Assert.assertFalse(data.getComponents().isEmpty());
		for (final Object element : components) {
			final TestbedStatus componentStatus = (TestbedStatus) element;

			if (componentStatus.getId().compareTo(this.firstComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.firstComponentStatusMessage);
				Assert.assertEquals(componentStatus.getLastCheck().getTime(),
						dateFirstComponentInStream.getTime());
				Assert.assertEquals("upAndLastCheckedOld",
						componentStatus.getStatus());
			}
			if (componentStatus.getId().compareTo(this.secondComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.secondComponentStatusMessage);
				Assert.assertEquals(componentStatus.getLastCheck().getTime(),
						dateInStream.getTime());
				Assert.assertEquals("up", componentStatus.getStatus());
			}
			if (componentStatus.getId().compareTo(this.thirdComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.thirdComponentStatusMessage);
				Assert.assertEquals(componentStatus.getLastCheck().getTime(),
						dateInStream.getTime());
				Assert.assertEquals("up", componentStatus.getStatus());
			}

		}
	}

	@Test
	public void testRunLastCheckInFuture() throws ParseException,
			FileNotFoundException {
		this.pushStream(this.getClass().getResourceAsStream(
				"/pushOMLStringLastCheckedInFuture.txt"));
		final Date dateInStream = this
				.parseStringToDate(this.firstComponentLastChecked);

		// last checked of secon component is in future

		final TestbedStatus data = this.statusPresenter
				.getTestBedStatusById(this.testbedName);
		Assert.assertNotNull(data);
		Assert.assertEquals(this.testbedName, data.getId());
		Assert.assertNotNull(data.getComponents());
		Assert.assertFalse(data.getComponents().isEmpty());

		Assert.assertEquals("up", data.getStatus());

		final Collection<TestbedStatus> components = data.getComponents();
		Assert.assertFalse(data.getComponents().isEmpty());
		for (final Object element : components) {
			final TestbedStatus componentStatus = (TestbedStatus) element;
			// second component has wrong set last checked, should not be listed

			Assert.assertFalse(componentStatus.getId().compareTo(
					this.secondComponentName) == 0);

			if (componentStatus.getId().compareTo(this.firstComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.firstComponentStatusMessage);
			}
			if (componentStatus.getId().compareTo(this.thirdComponentName) == 0) {
				Assert.assertEquals(componentStatus.getStatusMessage(),
						this.thirdComponentStatusMessage);
			}

			Assert.assertEquals(componentStatus.getLastCheck().getTime(),
					dateInStream.getTime());
		}
	}

	private void pushStream(final InputStream inputStreamOfText)
			throws ParseException {
		this.clientHandler = new ClientHandler(new BufferedReader(
				new InputStreamReader(inputStreamOfText)));
		this.clientHandler.setTestbedStatusCheck(this.testbedStatusCheck);
		this.clientHandler.run();
		try {
			Thread.sleep(2000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	private Date parseStringToDate(final String dateString)
			throws ParseException {
		final Calendar response = DatatypeConverter.parseDate(dateString);
		final Date result = new Date(response.getTimeInMillis());
		return result;
	}

}
