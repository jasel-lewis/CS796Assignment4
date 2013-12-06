package com.jasel.classes.cs796.assignment4.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import com.jasel.classes.cs796.assignment4.model.ClientType;
import com.jasel.classes.cs796.assignment4.model.Connection;
import com.jasel.classes.cs796.assignment4.view.MessageType;

/**
 * @author Jasel
 */
public class ClientSocketManager implements Runnable {
	private Connection connection = null;
	private ClientController controller = null;
	private InetAddress serverInetAddress = null;
	private ClientType clientType = null;
	private int serverPort;
	private boolean isConnected = false;
	private boolean running = false;
	
	
	public ClientSocketManager(
			ClientController controller,
			InetAddress serverInetAddress,
			int serverPort,
			ClientType clientType) {
		this.controller = controller;
		this.serverInetAddress = serverInetAddress;
		this.serverPort = serverPort;
		this.clientType = clientType;
	}
	
	
	
	@Override
	public void run() {
		running = true;

		try {
			controller.configureViewForConnectingState(true);
			connection = new Connection(serverInetAddress, serverPort);
			controller.configureViewForConnectedState(true);
			isConnected = true;
			controller.writeToLog("Established connection with UNOServer as a " + clientType + " client",
					MessageType.INFORMATIONAL);
		} catch (IOException ioe) {
			controller.configureViewForConnectingState(false);
			controller.errorHelper(ioe, "Unable to connect to UNOServer");
//			controller.disconnect();
		}
		
		if (isConnected && clientType.equals(ClientType.NORMAL)) {
			// Normal client, so close the connection we just made - this indicates to
			// UNOServer that we are a Normal client and it needs to call us back
			try {
				connection.close();
				controller.configureViewForConnectedState(false);
				isConnected = false;
				controller.writeToLog("Closed connection to let UNOServer know this is a " + clientType +
						" client", MessageType.WARNING);
			} catch (IOException ioe) {
				controller.errorHelper(ioe, "Could not close the connection");
			}
			
			// Create a Socket on the same local port that we just called out from.  This
			// is the Socket which the UNOServer will call back on.
			if (!isConnected) {
				try {
					controller.configureViewForConnectingState(true);
					connection = new Connection((new ServerSocket(connection.getLocalPort())).accept());
					controller.writeToLog("Received callback from UNOServer", MessageType.INFORMATIONAL);
					controller.configureViewForConnectedState(true);
					isConnected = true;
				} catch (IOException ioe) {
					controller.configureViewForConnectingState(false);
					controller.errorHelper(ioe, "Could not create a Socket for the UNOServer to call back to");
				}
			}
		}
		
		if (isConnected) {
			String input = connection.readLine();
			
			while (running) {
				if (input == null) {
					// Connection is dead
					//terminate();
					break;
				} else {
					controller.writeToLog(input, MessageType.NORMAL);
				}
				
				input = connection.readLine();
			}
		}
		
		controller.disconnect();
	}
	
	
	
	/**
	 * Write the passed message out to the Connection
	 * @param message
	 */
	public void writeToConnection(String message) {
		if (isConnected) {
			controller.writeToLog("Sending: " + message, MessageType.SUBDUED);
			connection.write(message);
		}
	}
	
	
	
	/**
	 * Prevent future echo-back with the socket, indicate that the server has
	 * chosen to terminate the connection, and close the socket
	 */
	public void terminate() {
		running = false;
		
		try {
			if (isConnected) {
				connection.shutdownInput();
				connection.close();
				isConnected = false;
			}
			
			controller.configureViewForConnectedState(false);
		} catch (IOException ioe) {
			// This exception might actually be normal because we're closing the socket
			// when readLine() is blocking
			controller.errorHelper(ioe, "Could not close the socket");
		}
	}
}