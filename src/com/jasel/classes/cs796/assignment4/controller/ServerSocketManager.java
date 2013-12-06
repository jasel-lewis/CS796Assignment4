/**
 * 
 */
package com.jasel.classes.cs796.assignment4.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import com.jasel.classes.cs796.assignment4.model.Connection;
import com.jasel.classes.cs796.assignment4.model.ConnectionTableModel;
import com.jasel.classes.cs796.assignment4.view.MessageType;

/**
 * @author Jasel
 *
 */
public class ServerSocketManager implements Runnable {
	private ServerSocket serverSocket = null;
	private ServerController controller = null;
	private ConnectionTableModel model = null;
	private List<Thread> threads = new ArrayList<Thread>();
	private List<ConnectionManager> connectionManagers = new ArrayList<ConnectionManager>();
	private volatile boolean running = false;
	
	public ServerSocketManager(ServerController controller, ConnectionTableModel model, int port) {
		try {
			this.controller = controller;
			this.model = model;
			serverSocket = new ServerSocket(port);
			controller.writeToLog("Created server socket: " + serverSocket, MessageType.SUBDUED);
		} catch (IOException e) {
			System.err.println("Cannot create ServerSocket on port " + port);
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public void run() {
		Connection connection = null;
		ConnectionManager clientSocketManager = null;
		Thread thread = null;
		
		running = true;
		
		controller.writeToLog("Server socket listening", MessageType.SUBDUED);
		
		while(running) {
			try {
				connection = new Connection(serverSocket.accept());

				clientSocketManager = new ConnectionManager(controller, model, connection);
				connectionManagers.add(clientSocketManager);
				
				thread = new Thread(clientSocketManager);
				thread.start();
				threads.add(thread);
			} catch (IOException ioe) {
				controller.writeToLog("Could not accept a new client connection", MessageType.ERROR);
				ioe.printStackTrace();
			}
		}
	}
	
	
	
	public void terminate() {
		int csmlSize = connectionManagers.size();
		int tlSize = threads.size();
		
		running = false;
		
		if (csmlSize != tlSize) {
			System.err.println("ClientSocketManager List and client Thread List out of synch - aborting...");
			System.exit(2);
		} else {
			for (int i = 0; i < csmlSize; i++) {
				connectionManagers.get(i).terminate();
				
				try {
					threads.get(i).join(2000);
				} catch (InterruptedException ie) {
					;  //TODO: Do nothing - normal Exception if interrupted (maybe? - could something better be done?)
				}
			}
			
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Could not close ServerSocket");
			}
			
			controller.writeToLog("Server socket closed", MessageType.WARNING);
		}
	}
}