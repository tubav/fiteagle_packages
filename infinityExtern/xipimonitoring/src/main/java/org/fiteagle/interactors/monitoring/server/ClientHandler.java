package org.fiteagle.interactors.monitoring.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.fiteagle.core.monitoring.StatusTable;
import org.fiteagle.interactors.monitoring.MonitoringManager;
import org.fiteagle.interactors.monitoring.TestbedStatusCheck;

public class ClientHandler implements Runnable {
	private TestbedStatusCheck testbedStatusCheck = new TestbedStatusCheck();
	Socket socket;
	Map<Integer, String> componentSchemaNames = new HashMap<Integer, String>();
	private final BufferedReader in;

	public ClientHandler(final BufferedReader in) {
		this.in = in;
	}

	public ClientHandler(final Socket s) throws IOException {
		this(new BufferedReader(new InputStreamReader(s.getInputStream(),
				StandardCharsets.UTF_8)));
		this.socket = s;
	}

	@Override
	public void run() {

		try {
			this.processRequest(this.in);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (this.socket != null) {
					this.socket.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

	}

	private void processRequest(final BufferedReader in) throws IOException,
			ParseException {
		String str;
		String testbedName = null;

		while ((str = in.readLine()) != null) {
			final StatusTable componentStatusTable = new StatusTable();

			str = str.trim();

			if (str.contains("domain:")) {
				testbedName = str.split(":")[1].trim();
			}

			if (str.startsWith("schema:")) {
				final String schemaNrAndName = str.split(":")[1].trim();
				Integer schemaNr = null;
				int startOfSchemaName;
				int i = 0;
				while (!Character.isDigit(schemaNrAndName.charAt(i))
						&& (schemaNrAndName.length() > i)) {
					i++;
				}
				if (i == (schemaNrAndName.length() - 1)) {
					throw new RuntimeException(
							"Schema definition in OML Stream is wrong");
				}
				if (Character.isDigit(schemaNrAndName.charAt(i + 1))) {
					schemaNr = Integer.parseInt(schemaNrAndName.substring(i,
							i + 1));
					startOfSchemaName = i + 2;
				} else {
					schemaNr = Integer.parseInt(new String(
							new char[] { schemaNrAndName.charAt(i) }));
					if (schemaNr == 0) {
						continue;
					}
					startOfSchemaName = i + 1;
				}
				// int endOfSchemaName = schemaNrAndName.lastIndexOf("node");
				final int endOfSchemaName = schemaNrAndName
						.lastIndexOf("statusMessage");

				final String schemaName = schemaNrAndName.substring(
						startOfSchemaName, endOfSchemaName).trim();
				this.componentSchemaNames.put(schemaNr, schemaName);
			}

			if ((str.length() > 0) && Character.isDigit(str.charAt(0))) {

				if ((testbedName == null) || (testbedName.compareTo("") == 0)) {
					throw new RuntimeException(
							"The testbed name must be set as domain!");
				}

				final String[] strArray = this.parseLine(str);

				Date lastCheckedDate = null;
				if (strArray[3] != null) {
					lastCheckedDate = this.parseStringToDate(strArray[3]);
					if (this.testbedStatusCheck
							.isLastCheckedInFuture(lastCheckedDate)) {
						continue;
					}
					if (this.testbedStatusCheck
							.isLastCheckedTooOld(lastCheckedDate)) {
						continue;
					}
				}
				componentStatusTable.setLastCheck(lastCheckedDate);
				componentStatusTable.setId(this.componentSchemaNames
						.get(new Integer(strArray[0])));

				componentStatusTable.setStatusMessage(strArray[1]);

				if (strArray[2].compareTo("1") == 0) {
					if (this.testbedStatusCheck
							.isLastCheckedOld(lastCheckedDate)) {
						componentStatusTable
								.setStatus(StatusTable.UP_AND_LAST_CHECKED_OLD);
					} else {
						componentStatusTable.setStatus(StatusTable.UP);
					}
				}

				if (strArray[2].compareTo("0") == 0) {
					componentStatusTable.setStatus(StatusTable.DOWN);
				}

				StatusTable statusTable = new MonitoringManager()
						.getMonitoringDataById(testbedName);

				if (statusTable == null) {
					statusTable = new StatusTable();
					statusTable.setId(testbedName);
					statusTable.addComponent(componentStatusTable);
					statusTable.setLastCheck(componentStatusTable
							.getLastCheck());
					statusTable.setStatus(componentStatusTable.getStatus());
				} else {
					statusTable.addComponent(componentStatusTable);
				}

				statusTable = this.testbedStatusCheck
						.updateStatusTableState(statusTable);
				new MonitoringManager().pushMonitoringData(statusTable);
			}

		}
	}

	private Date parseStringToDate(final String dateString)
			throws ParseException {
		final Calendar response = DatatypeConverter.parseDate(dateString);
		return new Date(response.getTimeInMillis());

	}

	private String[] parseLine(final String str) {
		if (str == null) {
			return null;
		}

		final String[] strArr = str.split("\t");
		final String[] response = new String[4];

		response[0] = strArr[1].trim();
		response[1] = strArr[3].trim();
		response[2] = strArr[4].trim();
		response[3] = strArr[5].trim();

		return response;
	}

	public void setTestbedStatusCheck(
			final TestbedStatusCheck testbedStatusCheck) {
		this.testbedStatusCheck = testbedStatusCheck;
	}
}
