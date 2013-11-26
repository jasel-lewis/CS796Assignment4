/**
 * 
 */
package com.jasel.classes.cs796.assignment4.model;

import java.net.Inet4Address;
import java.util.Date;

/**
 * @author Jasel
 *
 */
public class Connection {
	private Inet4Address inetAddress = null;
	private String hostName = null;
	private int port = 0;
	private Date timestamp = new Date();
	
	
	/**
	 * @return the date/time the connection was created
	 */
	protected Date getTimestamp() {
		/* TODO: Put a custom renderer on the timestamp column in the list
		 * http://www.tutorialspoint.com/java/java_date_time.htm
		 */
		return timestamp;
	}
	
	
	/**
	 * @return the IPv4 string representation of the IP address
	 */
	protected String getIPAddress() {
		return inetAddress.getHostAddress();
	}
	
	
	
	/**
	 * @return the host name
	 */
	protected String getHostName() {
		return hostName;
	}
	
	
	
	/**
	 * @param hostName the name of the host
	 */
	protected void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	
	
	/**
	 * @return the inetAddress
	 */
	protected Inet4Address getInetAddress() {
		return inetAddress;
	}
	
	
	
	/**
	 * @param inetAddress the inetAddress to set
	 */
	protected void setInetAddress(Inet4Address inetAddress) {
		this.inetAddress = inetAddress;
	}
	
	
	
	/**
	 * @return the port
	 */
	protected int getPort() {
		return port;
	}
	
	
	
	/**
	 * @param port the port number
	 */
	protected void setPort(int port) {
		this.port = port;
	}
}