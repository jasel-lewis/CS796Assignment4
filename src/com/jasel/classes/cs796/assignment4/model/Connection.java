/**
 * 
 */
package com.jasel.classes.cs796.assignment4.model;

import java.net.Socket;

/**
 * @author Jasel
 *
 */
public class Connection extends Socket {
	private String type = "NoTypeSpecified";
	
	public String getIPv4() {
		return getInetAddress().toString();
	}
	
	
	
	public String getType() {
		return type;
	}



	public void setType(String string) {
		type = string;
	}
}