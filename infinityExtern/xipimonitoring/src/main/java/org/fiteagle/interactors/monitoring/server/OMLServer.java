package org.fiteagle.interactors.monitoring.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.fiteagle.interactors.monitoring.Utils;

public class OMLServer implements Runnable {

	private boolean running = true;

	public void terminate() {
		this.running = false;
	}

	@Override
	public void run() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(Utils.getOmlServerPortNumber());
		} catch (final IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Socket socket = null;
		while (this.running) {
			try {
				socket = serverSocket.accept();
			} catch (final IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			try {
				(new Thread(new ClientHandler(socket))).start();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}
		try {
			socket.close();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

	}

}
