package com.jasel.classes.cs796.assignment4.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.jasel.classes.cs796.assignment4.model.Connection;
import com.jasel.classes.cs796.assignment4.model.ClientTableModel;
import com.jasel.classes.cs796.assignment4.view.MessageType;

/**
 * @author Jasel
 *
 */
public class ServerSocketManager implements Runnable {
	private ServerSocket serverSocket = null;
	private ServerController controller = null;
	private ClientTableModel model = null;
	private List<Thread> threads = new ArrayList<Thread>();
	private List<ServerConnectionManager> serverConnectionManagers = new ArrayList<ServerConnectionManager>();
	private volatile boolean running = false;
	
	public ServerSocketManager(ServerController controller, ClientTableModel model, int port) {
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
		ServerConnectionManager serverConnectionManager = null;
		Thread thread = null;
		
		running = true;
		
		controller.writeToLog("Server socket listening", MessageType.SUBDUED);
		
		while(running) {
			try {
				connection = new Connection(serverSocket.accept());

				serverConnectionManager = new ServerConnectionManager(controller, model, connection);
				serverConnectionManagers.add(serverConnectionManager);
				
				thread = new Thread(serverConnectionManager);
				threads.add(thread);
				thread.start();
			} catch (SocketException se) {
				;  // Do nothing - normal when we kill this thread
			} catch (IOException ioe) {
				controller.writeToLog("Could not accept a new connection", MessageType.ERROR);
				ioe.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * Terminate all ConnectionManagers and their underlying Clients
	 */
	public void terminate() {
		int scmlSize = serverConnectionManagers.size();
		int tlSize = threads.size();
		
		running = false;
		
		if (scmlSize != tlSize) {
			System.err.println("ServerConnectionManager List and client Thread List out of synch - aborting...");
			System.exit(2);
		} else {
			for (int i = 0; i < scmlSize; i++) {
				serverConnectionManagers.get(i).terminate();
				
				try {
					threads.get(i).join(2000);
				} catch (InterruptedException ie) {
					;  // Do nothing
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