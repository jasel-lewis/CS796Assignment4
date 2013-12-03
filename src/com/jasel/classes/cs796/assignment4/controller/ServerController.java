/**
 * @author Jasel
 */
package com.jasel.classes.cs796.assignment4.controller;

import com.jasel.classes.cs796.assignment4.model.Connection;
import com.jasel.classes.cs796.assignment4.model.ConnectionTableModel;
import com.jasel.classes.cs796.assignment4.view.ServerView;


public class ServerController {
	private ConnectionTableModel model = null;
	private ServerView view = null;
	private Thread thread = null;
	private ServerSocketManager serverSocketManager = null;
	boolean isListening = false;
	int testPort = 4587;  //TODO: remove this - test purposes

	public ServerController(ConnectionTableModel model, ServerView view) {
		//this.connections = connections;
		this.model = model;
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
		serverSocketManager = new ServerSocketManager(model, port);
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
		//connections.add(new Connection());
		//view.getTableModel().fireTableChanged(new TableModelEvent(view.getTableModel()));
		model.addConnection(new Connection());
	}
}