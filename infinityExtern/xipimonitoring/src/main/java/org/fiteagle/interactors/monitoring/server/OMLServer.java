package org.fiteagle.interactors.monitoring.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.fiteagle.interactors.monitoring.MonitoringManager;
import org.fiteagle.interactors.monitoring.Utils;

public class OMLServer implements Runnable {
	

	private boolean running = true;
	
	public void terminate(){
		this.running = false;
	}
	
	public void run() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(Utils.OML_SERVER_PORT_NUMBER);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		};
		
		Socket socket = null;
		while (running) {
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			try {
				(new Thread(new ClientHandler(socket))).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e.getMessage());
			}
		}
		try {
			socket.close();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		
		
	}


}
