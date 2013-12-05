/**
 * 
 */
package com.jasel.classes.cs796.assignment4.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.jasel.classes.cs796.assignment4.model.ClientType;
import com.jasel.classes.cs796.assignment4.view.ClientView;

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
			// Validate the user-provided IPv4 address
			if (InetAddressValidator.getInstance().isValidInet4Address(unverifiedIPAddress)) {
				try {
					connect(InetAddress.getByName(unverifiedIPAddress), port, clientType);
				} catch (UnknownHostException e) {
					// TODO: Create pop-up stating unknown host
					renderWarningDialog("Unknown Host",
							"The provided IP address cannot be resolved to a valid host");
				}
			} else {
				renderWarningDialog("Invalid IPv4 Address",
						"The provided IP address is not in a valid IPv4 format");
			}
		}
	}
	
	
	
	public void handleSendClick(String message) {
		if (!message.equals("")) {
			view.clearMessage();
			clientSocketManager.writeToConnection(message);
		}
	}
	
	
	
	private void connect(InetAddress inetAddress, int port, ClientType clientType) {
		clientSocketManager = new ClientSocketManager(this, inetAddress, port, clientType);
		thread = new Thread(clientSocketManager);
		thread.start();
	}
	
	
	
	private void disconnect() {
		if ((thread != null) && (clientSocketManager != null)) {
			clientSocketManager.terminate();
			
			try {
				thread.join(1000);
			} catch (InterruptedException ie) {
				;  // Do nothing - normal Exception if interrupted
			}
			
			clientSocketManager = null;
		}
	}
	
	
	
	public void errorHelper(Exception exception, String preText) {
		String message = exception.getMessage();
		
		view.writeToLog(preText + ((message.equals("")) ? "" : (": " + message)));
	}
	
	
	
	public synchronized void writeToLog(String text) {
		view.writeToLog(text + "\n");
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
}