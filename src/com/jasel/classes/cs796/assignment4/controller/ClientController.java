/**
 * 
 */
package com.jasel.classes.cs796.assignment4.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import com.jasel.classes.cs796.assignment4.model.ClientType;
import com.jasel.classes.cs796.assignment4.view.ClientView;
import com.jasel.classes.cs796.assignment4.view.MessageType;

/**
 * @author Jasel
 */
public class ClientController {
	private ClientView view = null;
	private ClientSocketManager clientSocketManager = null;
	private Thread thread = null;

	
	public ClientController(ClientView view) {
		this.view = view;
	}

	
	
	public void handleConnectClick(String unverifiedIPAddress, int port, ClientType clientType) {
		if (clientSocketManager != null) {
			disconnect();
		} else {
			try {
				connect(InetAddress.getByName(unverifiedIPAddress), port, clientType);
			} catch (UnknownHostException e) {
				// TODO: Create pop-up stating unknown host
				renderWarningDialog("Unknown Host",
						"The provided IP address cannot be resolved to a valid host");
			}
		}
	}
	
	
	
	public void handleSendClick(String message) {
		if (!message.equals("") && (clientSocketManager != null)) {
			view.clearMessage();
			clientSocketManager.writeToConnection(message);
		}
	}
	
	
	
	private void connect(InetAddress inetAddress, int port, ClientType clientType) {
		clientSocketManager = new ClientSocketManager(this, inetAddress, port, clientType);
		thread = new Thread(clientSocketManager);
		thread.start();
	}
	
	
	
	protected void disconnect() {
		if ((thread != null) && (clientSocketManager != null)) {
			clientSocketManager.terminate();
			
			try {
				thread.join(1000);
			} catch (InterruptedException ie) {
				;  // Do nothing - normal Exception if interrupted
			}
			
			writeToLog("Disconnected from UNOServer", MessageType.SUBDUED);
			
			clientSocketManager = null;
		}
	}
	
	
	
	public void errorHelper(Exception exception, String preText) {
		String message = exception.getLocalizedMessage();
		
		writeToLog(preText + ((message.equals("")) ? "" : (": " + message)), MessageType.ERROR);
	}
	
	
	
	public synchronized void writeToLog(String message, MessageType messageType) {
		view.writeToLog(message + "\n", messageType);
	}



	public void handleClearLogClick() {
		view.clearLog();
	}
	
	
	
	private void renderWarningDialog(String title, String message) {
		JOptionPane.showMessageDialog(view, message, title, JOptionPane.WARNING_MESSAGE);
	}
	
	
	
	public void configureViewForConnectedState(boolean connected) {
		view.configureForConnectedState(connected);
	}
	
	
	
	public void configureViewForConnectingState(boolean connecting) {
		view.configureForConnectingState(connecting);
	}
}