/**
 * @author Jasel
 */
package com.jasel.classes.cs796.assignment4.controller;

import java.util.Vector;

import com.jasel.classes.cs796.assignment4.model.Connection;
import com.jasel.classes.cs796.assignment4.view.ServerView;


public class ServerController {
	private Vector<Connection> connections = null;
	private ServerView view = null;
	private Thread thread = null;
	private ServerSocketManager serverSocketManager = null;
	boolean isListening = false;

	public ServerController(Vector<Connection> connections, ServerView view) {
		this.connections = connections;
		this.view = view;
	}

	
	
	public void handleListenViewItemClick(int port) {
		if (isListening) {
			stopListening();
		} else {
			startListening(port);
		}
	}
	
	
	
	private void stopListening() {
		if (thread != null) {
			serverSocketManager.terminate();
			
			try {
				thread.join(2000);
			} catch (InterruptedException ie) {
				;  // Do nothing - normal Exception if interrupted
			}
		}
		
		view.configureForListeningState(false);
		isListening = false;
	}
	
	
	
	private void startListening(int port) {
		serverSocketManager = new ServerSocketManager(connections, port);
		thread = new Thread(serverSocketManager);
		thread.start();
		
		view.configureForListeningState(true);
		isListening = true;
	}



	public void handleClearLogButtonClick() {
		view.clearLog();
	}
	
	
	
	//TODO: Remove this
	public void testAddConnectionsToView() {
		//TODO: Was working here when left off.  Generate fake Connections, add them to the
		//model and see if the JTable remains in synch
	}
}