/**
 * 
 */
package com.jasel.classes.cs796.assignment4.controller;

import com.jasel.classes.cs796.assignment4.model.ConnectionListModel;
import com.jasel.classes.cs796.assignment4.view.ServerFrame;

/**
 * @author Jasel
 *
 */
public class ServerController {
	ConnectionListModel model;
	ServerFrame view;
	
	public ServerController(ConnectionListModel model, ServerFrame view) {
		this.model = model;
		this.view = view;
	}
	
	
	
	
	/**
	 * @return model
	 */
	public ConnectionListModel getModel() {
		return model;
	}
}