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

	
	
	/**
	 * Either connect or disconnect to/from the server based on the current state
	 * @param unverifiedIPAddress
	 * @param port
	 * @param clientType
	 */
	public void handleConnectClick(String unverifiedIPAddress, int port, ClientType clientType) {
		if (clientSocketManager != null) {
			disconnect();
		} else {
			try {
				connect(InetAddress.getByName(unverifiedIPAddress), port, clientType);
			} catch (UnknownHostException e) {
				renderWarningDialog("Unknown Host",
						"The provided IP address cannot be resolved to a valid host");
			}
		}
	}
	
	
	
	/**
	 * Send the user's message to the ClientSocketManager for sending out over the
	 * Connection
	 * @param message
	 */
	public void handleSendClick(String message) {
		if (!message.equals("") && (clientSocketManager != null)) {
			view.clearMessage();
			clientSocketManager.writeToConnection(message);
		}
	}
	
	
	
	/**
	 * Create the ClientSocketManager and let it run in its own thread
	 * @param inetAddress
	 * @param port
	 * @param clientType
	 */
	private void connect(InetAddress inetAddress, int port, ClientType clientType) {
		clientSocketManager = new ClientSocketManager(this, inetAddress, port, clientType);
		thread = new Thread(clientSocketManager);
		thread.start();
	}
	
	
	
	/**
	 * Disconnect from the server - destroy the ClientSocketManager
	 */
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
	
	
	
	/**
	 * Helper utility to condense common functionality
	 * @param exception
	 * @param preText
	 */
	public void errorHelper(Exception exception, String preText) {
		String message = exception.getLocalizedMessage();
		
		writeToLog(preText + ((message.equals("")) ? "" : (": " + message)), MessageType.ERROR);
	}
	
	
	
	/**
	 * Have the ClientView write the passed message to the log
	 * @param message
	 * @param messageType
	 */
	public synchronized void writeToLog(String message, MessageType messageType) {
		view.writeToLog(message + "\n", messageType);
	}



	public void handleClearLogClick() {
		view.clearLog();
	}
	
	
	
	/**
	 * Create a modal warning dialog with the passed title and message
	 * @param title
	 * @param message
	 */
	private void renderWarningDialog(String title, String message) {
		JOptionPane.showMessageDialog(view, message, title, JOptionPane.WARNING_MESSAGE);
	}
	
	
	
	/**
	 * Run actions on the View to put it in a connected or disconnected state
	 * @param connected
	 */
	public void configureViewForConnectedState(boolean connected) {
		view.configureForConnectedState(connected);
	}
	
	
	
	/**
	 * Run actions on the view to put it in a connecting (wait) status as the connection
	 * is established
	 * @param connecting
	 */
	public void configureViewForConnectingState(boolean connecting) {
		view.configureForConnectingState(connecting);
	}
}