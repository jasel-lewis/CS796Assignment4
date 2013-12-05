/**
 * 
 */
package com.jasel.classes.cs796.assignment4.controller;

import com.jasel.classes.cs796.assignment4.view.ClientView;

/**
 * @author Jasel
 */
public class ClientController {
	private ClientView view = null;
	boolean isConnected = false;

	public ClientController(ClientView view) {
		this.view = view;
	}

	
	
	public void handleConnectClick(int port) {
		if (isConnected) {
			disconnect();
		} else {
			connect(port);
		}
	}
	
	
	
	public void handleSendClick(String message) {
		;  //TODO: something here...
	}
	
	
	
	private void disconnect() {
		;  // manipulate the connection...
		
		view.configureForConnectState(false);
		isConnected = false;
	}
	
	
	
	private void connect(int port) {
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