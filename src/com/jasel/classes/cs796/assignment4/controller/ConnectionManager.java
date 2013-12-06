package com.jasel.classes.cs796.assignment4.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jasel.classes.cs796.assignment4.model.ClientType;
import com.jasel.classes.cs796.assignment4.model.Connection;
import com.jasel.classes.cs796.assignment4.model.ConnectionTableModel;
import com.jasel.classes.cs796.assignment4.view.MessageType;

/**
 * @author Jasel
 */
public class ConnectionManager implements Runnable {
	private ServerController controller = null;
	private ConnectionTableModel model = null;
	private Connection connection = null;
	private volatile boolean running = false;
	
	
	public ConnectionManager(ServerController controller, ConnectionTableModel model, Connection connection) {
		this.controller = controller;
		this.model = model;
		this.connection = connection;
		model.addConnection(connection);
		controller.writeToLog(connection.toString(), MessageType.INFORMATIONAL);
		controller.writeToLog("Connection is assumed to be Urgent at this point, won't know otherwise " +
				"until the blocking call reading the socket returns", MessageType.SUBDUED);
	}
	
	
	
	@Override
	public void run() {
		String input = null;
		
		running = true;
		
		try {
			// Pause a quarter of a second, giving the Connection a chance to close if
			// it's a non-Urgent (Normal) client and open its own listening Socket
			Thread.sleep(250);
		} catch (InterruptedException e) {
			;  // Do nothing
		}
		
		input = connection.readLine();
		
		if (input == null) {
			// Client has closed the Connection.  This means either it is a NORMAL client which
			// is now waiting to be called back or the Connection was aborted.
			try {
				model.removeConnection(connection);
				controller.writeToLog("Connection closed with no message transmission - could be a " +
						"Normal client - attempting to call back", MessageType.WARNING);
				
				// Create a new Connection back to the IP and port which was just connected to us
				connection = new Connection(connection.getInetAddress(), connection.getPort());
				connection.setType(ClientType.NORMAL);
				model.addConnection(connection);
				controller.writeToLog(connection.toString(), MessageType.INFORMATIONAL);
				input = connection.readLine();
			} catch (IOException e) {
				// Could not create the new Connection to call-back the client - must be that the
				// client was an aborted Connection
				running = false;
				controller.writeToLog("Unable to create a return Connection to the (assumed) Normal client", MessageType.ERROR);
			}
		}

		while(running) {
			if (input != null) {
				// Echo back the string
				connection.write("echoback> " + input);
			} else {
				//controller.writeToLog("Connection has terminated", MessageType.ERROR);
				break;
			}
			
			input = connection.readLine();
		}
		
		closeConnection();
		controller.writeToLog("Connection remotely terminated", MessageType.ERROR);
	}
	
	
	
	/**
	 * Prevent future echo-back with the socket, indicate that the server has
	 * chosen to terminate the connection, and close the socket
	 */
	public void terminate() {
		running = false;
		
		if (connection != null) {
			connection.write(generateSystemMessage("Server has terminated the connection.  Goodbye..."));
			closeConnection();
		}
	}
	
	
	
	/**
	 * Helper utility to close the Connection and ensure removal of the Connection from
	 * the ListModel
	 */
	private void closeConnection() {
		try {
			connection.close();
			model.removeConnection(connection);
		} catch (IOException e) {
			// This exception might actually be normal because we're closing the socket
			//when readLine() is blocking
			System.err.println("Could not close the socket.  Runaway socket.");
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Utility to prepend time information to a message being sent to the client from
	 * the server
	 * @param message
	 * @return
	 */
	private String generateSystemMessage(String message) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		
		return " (" + sdf.format(date) + "):  " + message;
	}
}