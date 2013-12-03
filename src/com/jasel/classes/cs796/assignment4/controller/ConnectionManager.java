/**
 * 
 */
package com.jasel.classes.cs796.assignment4.controller;

import java.io.IOException;
import com.jasel.classes.cs796.assignment4.model.Connection;
import com.jasel.classes.cs796.assignment4.model.ConnectionTableModel;

/**
 * @author Jasel
 */
public class ConnectionManager implements Runnable {
	private ServerController controller = null;  //TODO: this is solely to write to the log - make a static Log object that fires update listeners to the view's log
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

		while(running) {
			input = connection.readLine();

			if (input != null) {
				// Echo back the string
				connection.write(input, false);
			} else {
				break;
			}
		}
	}
	
	
	
	/**
	 * Prevent future echo-back with the socket, indicate that the server has
	 * chosen to terminate the connection, and close the socket
	 */
	public void terminate() {
		running = false;
		
		if (connection != null) {
			connection.write("Server has terminated the connection.  Goodbye...", true);
			try {
				connection.close();
				model.removeConnection(connection);
			} catch (IOException e) {
				System.err.println("Could not close the socket.  Runaway socket.");
				e.printStackTrace();
			}
		}
	}
}