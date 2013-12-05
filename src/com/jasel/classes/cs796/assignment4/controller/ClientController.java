/**
 * 
 */
package com.jasel.classes.cs796.assignment4.controller;

import java.io.IOException;

import com.jasel.classes.cs796.assignment4.model.ClientType;
import com.jasel.classes.cs796.assignment4.model.Connection;
import com.jasel.classes.cs796.assignment4.view.ClientView;

/**
 * @author Jasel
 */
public class ClientController {
	private boolean isConnected = false;
	
	private ClientView view = null;
	private Connection connection = null;

	public ClientController(ClientView view) {
		this.view = view;
	}

	
	
	public void handleConnectClick(String unverifiedIPAddress, int port, ClientType clientType) {
		// attempt to convert IP address to an Inet4Address
		if (isConnected) {
			disconnect();
		} else {
			connect(port, clientType);
		}
	}
	
	
	
	public void handleSendClick(String message) {
		connection.write(message);
	}
	
	
	
	private void disconnect() {
		try {
			connection.close();
			view.configureForConnectState(false);
			isConnected = false;
		} catch (IOException e) {
			String error = "Could not close the Connection - runaway connection...";
			view.appendToLog(error);
			e.printStackTrace();
		}
	}
	
	
	
	private void connect(int port, ClientType clientType) {
		;  // manipulate the connection...
		
		view.configureForConnectState(true);
		isConnected = true;
	}
	
	
	
	public synchronized void writeToLog(String text) {
		view.appendToLog(text + "\n");
	}



	public void handleClearLogClick() {
		view.clearLog();
	}
}