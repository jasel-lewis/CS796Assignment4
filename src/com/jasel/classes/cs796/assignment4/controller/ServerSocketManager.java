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

/**
 * @author Jasel
 *
 */
public class ServerSocketManager implements Runnable {
	private ServerSocket serverSocket = null;
	private ConnectionTableModel model = null;
	private List<Thread> threadList = new ArrayList<Thread>();
	private List<ClientSocketManager> clientSocketManagerList = new ArrayList<ClientSocketManager>();
	private volatile boolean running = false;
	
	public ServerSocketManager(ConnectionTableModel model, int port) {
		try {
			this.model = model;
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Cannot create ServerSocket on port " + port);
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public void run() {
		running = true;
		
		while(running) {
			Connection connection = null;
			ClientSocketManager clientSocketManager = null;
			Thread thread = null;
			
			try {
				connection = (Connection)serverSocket.accept();
				connection.setType("unspecified");
				clientSocketManager = new ClientSocketManager(model, connection);
				clientSocketManagerList.add(clientSocketManager);
				
				thread = new Thread(clientSocketManager);
				thread.start();
				threadList.add(thread);
			} catch (IOException ioe) {
				System.err.println("Could not accept() a new client connection");
			}
		}
	}
	
	
	
	public void terminate() {
		int csmlSize = clientSocketManagerList.size();
		int tlSize = threadList.size();
		
		running = false;
		
		if (csmlSize != tlSize) {
			System.err.println("ClientSocketManager List and client Thread List out of synch - aborting...");
			System.exit(2);
		} else {
			for (int i = 0; i < csmlSize; i++) {
				clientSocketManagerList.get(i).terminate();
				
				try {
					threadList.get(i).join(2000);
				} catch (InterruptedException ie) {
					;  // Do nothing - normal Exception if interrupted
				}
			}
		}
	}
}