package org.fiteagle.interactors.monitoring.client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.fiteagle.interactors.monitoring.Utils;

public class OMLClientMock {

	String hostName = Utils.OML_SERVER_HOSTNAME;
	int portNumber = Utils.OML_SERVER_PORT_NUMBER;
	String pathToPushOMLString = "/pushOMLString.txt";
	private String pathToOMLStreamFile = null;
	
	
	InputStream inputStream = null;
	Socket socket = null;
	PrintWriter out = null;

	public OMLClientMock() {
	}

	public OMLClientMock(String hostName, int portNumber) {
		this.hostName = hostName;
		this.portNumber = portNumber;
	}

	public OMLClientMock(String hostName, int portNumber,
			InputStream inputStream) {
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.inputStream = inputStream;
	}

	public OMLClientMock(String hostName, int portNumber, String pathToFile) {
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.pathToOMLStreamFile = pathToFile;

	}

	public void run() {
		if (this.inputStream == null) {
			try {
				inputStream = getInputStream();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		processInputStream(inputStream);
	}

	private void processInputStream(InputStream inputStream) {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				inputStream));
		try {
			socket = new Socket(hostName, portNumber);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}

		String line;
		try {
			while ((line = in.readLine()) != null) {
				out.println(line);
				out.flush();
			}
		} catch (IOException e1) {
			throw new RuntimeException(e1.getMessage());
		}

		out.close();

		try {
			socket.close();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private InputStream getInputStream() throws FileNotFoundException {
		if (pathToOMLStreamFile == null)
			return this.getClass().getResourceAsStream(pathToPushOMLString);
		return new FileInputStream(pathToOMLStreamFile);

	}

}
