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
		//return getInetAddress().toString();
		return new String("test");
	}
	
	
	
	public String getType() {
		return type;
	}
	
	
	
	//TODO: get rid of this - testing
	public int getPort() {
		return 0;
	}



	public void setType(String string) {
		type = string;
	}
}