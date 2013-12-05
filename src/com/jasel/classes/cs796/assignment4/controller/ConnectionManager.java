/**
 * 
 */
package com.jasel.classes.cs796.assignment4.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jasel.classes.cs796.assignment4.model.ClientType;
import com.jasel.classes.cs796.assignment4.model.Connection;
import com.jasel.classes.cs796.assignment4.model.ConnectionTableModel;

/**
 * @author Jasel
 */
public class ConnectionManager implements Runnable {
	private ServerController controller = null;  //TODO: this is being passed all the way down to this class solely to write to the log - make a static Log object that fires update listeners to the view's log
	private ConnectionTableModel model = null;
	private Connection connection = null;
	private volatile boolean running = false;
	
	public ConnectionManager(ServerController controller, ConnectionTableModel model, Connection connection) {
		this.controller = controller;
		this.model = model;
		this.connection = connection;
		model.addConnection(connection);
		controller.writeToLog("Client connected: " + connection);
	}
	
	
	
	@Override
	public void run() {
		String input = null;
		
		running = true;
		
		try {
			// Pause half a second, giving the Connection a chance to close if it's
			// a non-Urgent (Normal) client and open its own listening Socket
			Thread.sleep(500);
		} catch (InterruptedException e) {
			;  // Do nothing
		}
		
		input = connection.readLine();
		
		if (input == null) {  // TODO: better here is input.equals("") (or a negation thereof)
			// Client has closed the Connection.  This means either it is a NORMAL client which
			// is now waiting to be called back or the Connection was aborted.
			try {
				model.removeConnection(connection);
				controller.writeToLog("Connection closed with no message transmission - could be a Normal client - attempting to call back");
				
				// Create a new Connection back to the IP and port which was just connected to us
				connection = new Connection(connection.getInetAddress(), connection.getPort());
				connection.setType(ClientType.NORMAL);
				model.addConnection(connection);
				controller.writeToLog("Normal client connected on " + connection);
				input = connection.readLine();
			} catch (IOException e) {
				// Could not create the new Connection to call-back the client - must be that the
				// client was an aborted Connection
				running = false;
				//TODO: may have to avoid the fall-through below to the closeConnection() call...
				//TODO: not sure if we need this next System.err.println() message
				System.err.println("Unable to create a return Connection to the (assumed) Normal client");
			}
		}

		while(running) {
			if (input != null) {  // TODO: better here is input.equals("") (or a negation thereof)
				// Echo back the string
				connection.write("echoback> " + input);
			} else {
				controller.writeToLog("Connection has terminated");
				break;
			}
			
			input = connection.readLine();
		}
		
		closeConnection();
		controller.writeToLog("Connection remotely terminated:  input=null?" + (input == null) + ";  running: " + running);
	}
	
	
	
	/**
	 * Prevent future echo-back with the socket, indicate that the server has
	 * chosen to terminate the connection, and close the socket
	 */
	//TODO: Need to test this
	public void terminate() {
		running = false;
		
		if (connection != null) {
			connection.write(generateSystemMessage("Server has terminated the connection.  Goodbye..."));
			closeConnection();
		}
	}
	
	
	
	private void closeConnection() {
		try {
			connection.close();
			model.removeConnection(connection);
		} catch (IOException e) {
			// TODO: this exception might actually be normal because we're closing the socket when readLine() is blocking
			System.err.println("Could not close the socket.  Runaway socket.");
			e.printStackTrace();
		}
	}
	
	
	
	private String generateSystemMessage(String message) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		
		return " (" + sdf.format(date) + "):  " + message;
	}
}