package org.fiteagle.interactors.monitoring.client;

import java.io.BufferedReader;
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

	String hostName = Utils.getOmlServerHostName();
	int portNumber = Utils.getOmlServerPortNumber();
	private String pathToPushOMLString = "/pushOMLString.txt";
	private String pathToOMLStreamFile = null;

	InputStream inputStream = null;
	Socket socket = null;
	PrintWriter out = null;

	public OMLClientMock() {
	}

	public OMLClientMock(final String hostName, final int portNumber) {
		this.hostName = hostName;
		this.portNumber = portNumber;
	}

	public OMLClientMock(final String hostName, final int portNumber,
			final InputStream inputStream) {
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.inputStream = inputStream;
	}

	public OMLClientMock(final String hostName, final int portNumber,
			final String pathToFile) {
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.pathToOMLStreamFile = pathToFile;

	}

	public void run() {
		if (this.inputStream == null) {
			try {
				this.inputStream = this.getInputStream();
			} catch (final FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}

		this.processInputStream(this.inputStream);
	}

	private void processInputStream(final InputStream inputStream) {
		final BufferedReader in = new BufferedReader(new InputStreamReader(
				inputStream));
		try {
			this.socket = new Socket(this.hostName, this.portNumber);
		} catch (final UnknownHostException e) {
			throw new RuntimeException(e.getMessage());
		} catch (final IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		try {
			this.out = new PrintWriter(this.socket.getOutputStream(), true);
		} catch (final IOException e) {
			throw new RuntimeException(e.getMessage());
		}

		String line;
		try {
			while ((line = in.readLine()) != null) {
				this.out.println(line);
				this.out.flush();
			}
		} catch (final IOException e1) {
			throw new RuntimeException(e1.getMessage());
		}

		this.out.close();

		try {
			this.socket.close();
		} catch (final IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private InputStream getInputStream() throws FileNotFoundException {
		if (this.pathToOMLStreamFile == null) {
			return this.getClass().getResourceAsStream(
					this.getPathToPushOMLString());
		}
		return new FileInputStream(this.pathToOMLStreamFile);

	}

	public String getPathToPushOMLString() {
		return this.pathToPushOMLString;
	}

	public void setPathToPushOMLString(final String pathToPushOMLString) {
		this.pathToPushOMLString = pathToPushOMLString;
	}

}
