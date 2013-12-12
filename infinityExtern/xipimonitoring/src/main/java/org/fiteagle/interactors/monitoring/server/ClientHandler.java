package org.fiteagle.interactors.monitoring.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.bind.DatatypeConverter;

import org.fiteagle.core.monitoring.StatusTable;
import org.fiteagle.interactors.monitoring.MonitoringManager;
import org.fiteagle.interactors.monitoring.TestbedStatusCheck;
import org.fiteagle.interactors.monitoring.Utils;

public class ClientHandler implements Runnable {
	Socket socket;

	public ClientHandler(Socket s) {
		socket = s;
	}

	public void run() {
		
		BufferedReader in;
		PrintWriter out = null;
		HashMap <Integer, String> componentSchemaNames = new HashMap<Integer, String>();
		
		
		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			String str;
			String testbedName = null;

			while ((str = in.readLine()) != null) {
				StatusTable componentStatusTable = new StatusTable();
				
				str = str.trim();

				if (str.contains("domain:"))
					testbedName = str.split(":")[1].trim();
				
				if(str.startsWith("schema:")){
					String schemaNrAndName = str.split(":")[1].trim();
					Integer schemaNr = null;
					int startOfSchemaName;
					int i = 0;
					while (!Character.isDigit(schemaNrAndName.charAt(i)) && schemaNrAndName.length() > i) i++;
					if(i==schemaNrAndName.length()-1) 
						throw new RuntimeException("Schema definition in OML Stream is wrong");
					if(Character.isDigit(schemaNrAndName.charAt(i+1))){
						schemaNr = Integer.parseInt(schemaNrAndName.substring(i, i+1));
						startOfSchemaName = i+2;
					}else{
						schemaNr = Integer.parseInt(new String(new char[]{schemaNrAndName.charAt(i)}));
						if(schemaNr == 0) continue;
						startOfSchemaName = i+1;
					}
//					int endOfSchemaName = schemaNrAndName.lastIndexOf("node");
					int endOfSchemaName = schemaNrAndName.lastIndexOf("statusMessage");
					
					String schemaName = schemaNrAndName.substring(startOfSchemaName, endOfSchemaName).trim();
					componentSchemaNames.put(schemaNr, schemaName);
				}
					
					

				if (str.length() > 0 && Character.isDigit(str.charAt(0))) {
					
					if (testbedName == null || testbedName == "")
						throw new RuntimeException(
								"The testbed name must be set as domain!");

					String[] strArray = parseLine(str);

					Date lastCheckedDate = null;
					if (strArray[3] != null){
						lastCheckedDate = parseStringToDate(strArray[3]);
						//TODO: check if the last checked is in future!
						if(new TestbedStatusCheck().isLastCheckedTooOld(lastCheckedDate)) continue;
					}
					componentStatusTable.setLastCheck(lastCheckedDate);
//					componentStatusTable.setId(strArray[1]);
					componentStatusTable.setId(componentSchemaNames.get(new Integer(strArray[0])));
					
					componentStatusTable.setStatusMessage(strArray[1]);
					
					if (strArray[2].compareTo("1") == 0) {
						if (new TestbedStatusCheck().isLastCheckedOld(lastCheckedDate)) {
							componentStatusTable.setStatus(StatusTable.UP_AND_LAST_CHECKED_OLD);
						} else
							componentStatusTable.setStatus(StatusTable.UP);
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

					statusTable = new TestbedStatusCheck().updateStatusTableState(statusTable);
					new MonitoringManager().pushMonitoringData(statusTable);
				}

			}


		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			out.close();
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}

	}

	private Date parseStringToDate(String dateString) throws ParseException {
//		SimpleDateFormat simpleDate = new SimpleDateFormat(
//				"yyyy-MM-dd'T'HH:mm:ss.SSSSSSz");
//
//		StringBuilder dateStrBuilder = new StringBuilder(dateString);
//		dateStrBuilder.deleteCharAt(dateString.lastIndexOf(":"));
//
//		return simpleDate.parse(dateStrBuilder.toString());
//		
		Calendar response = DatatypeConverter.parseDate(dateString);
		return new Date(response.getTimeInMillis());

	}

	private String[] parseLine(String str) {
		if (str == null)
			return null;

		String[] strArr = str.split("\t");
		String[] response = new String[4];

		response[0] = strArr[1].trim();
		response[1] = strArr[3].trim();
		response[2] = strArr[4].trim();
		response[3] = strArr[5].trim();

		return response;
	}
}
