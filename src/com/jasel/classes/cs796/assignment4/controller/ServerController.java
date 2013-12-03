/**
 * @author Jasel
 */
package com.jasel.classes.cs796.assignment4.controller;

import com.jasel.classes.cs796.assignment4.model.ConnectionTableModel;
import com.jasel.classes.cs796.assignment4.view.ServerView;


public class ServerController {
	private ConnectionTableModel model = null;
	private ServerView view = null;
	private Thread thread = null;
	private ServerSocketManager serverSocketManager = null;
	boolean isListening = false;

	public ServerController(ConnectionTableModel model, ServerView view) {
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
		serverSocketManager = new ServerSocketManager(this, model, port);
		thread = new Thread(serverSocketManager);
		thread.start();
		
		view.configureForListeningState(true);
		isListening = true;
	}
	
	
	
	public synchronized void writeToLog(String text) {
		view.appendToLog(text + "\n");
	}



	public void handleClearLogButtonClick() {
		view.clearLog();
	}
}