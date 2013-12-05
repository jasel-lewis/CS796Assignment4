/**
 * 
 */
package com.jasel.classes.cs796.assignment4.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import com.jasel.classes.cs796.assignment4.model.ClientType;
import com.jasel.classes.cs796.assignment4.model.Connection;

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
		boolean hasErrored = false;

		try {
			connection = new Connection(serverInetAddress, serverPort);
			controller.configureViewForConnectedState(true);
			isConnected = true;
			controller.writeToLog("Established connection with UNOServer as a " + clientType + " client");
		} catch (IOException ioe) {
			controller.writeToLog("Unable to connect to UNOServer: connection refused");
			hasErrored = true;
		}
		
		if (!hasErrored && clientType.equals(ClientType.NORMAL)) {
			// Normal client, so close the connection we just made - this indicates to
			// UNOServer that we are a Normal client and it needs to call us back
			try {
				connection.close();
				controller.configureViewForConnectedState(false);
				isConnected = false;
				controller.writeToLog("Closed connection to let it know this is a " + clientType + " client");
			} catch (IOException ioe) {
				controller.errorHelper(ioe, "Could not close the connection");
				hasErrored = true;
			}
			
			// Create a Socket on the same local port that we just called out from.  This
			// is the Socket which the UNOServer will call back on.
			if (!hasErrored) {
				try {
					connection = new Connection((new ServerSocket(connection.getLocalPort())).accept());
					controller.configureViewForConnectedState(true);
					isConnected = true;
				} catch (IOException ioe) {
					controller.errorHelper(ioe, "Could not create a Socket for the UNOServer to call back to");
					hasErrored = true;
				}
			}
		}
		
		if (!hasErrored) {
			String input = connection.readLine();
			
			if (input.equals("")) {
				// Connection is dead
				try {
					connection.close();
				} catch (IOException e) {
					;  // Do nothing...
				}
			} else {
				
			}
		}
	}
}