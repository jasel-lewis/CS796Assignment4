package com.jasel.classes.cs796.assignment4.model;

/**
 * @author Jasel
 */
public class Client {
	Connection connection = null;;
	private ClientType clientType = null;
	
	
	public Client(Connection connection) {
		this(connection, ClientType.UNSPECIFIED);
	}
	
	
	
	public Client(Connection connection, ClientType clientType) {
		this.connection = connection;
		this.clientType = clientType;
	}
	
	
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	
	
	public ClientType getClientType() {
		return clientType;
	}
	
	
	
	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}
	
	
	
	public Connection getConnection() {
		return connection;
	}
	
	
	
	public String read() {
		return connection.readLine();
	}
	
	
	
	public void write(String message) {
		connection.write(message);
	}
	
	
	
	public int getRemotePort() {
		return connection.getRemotePort();
	}
	
	
	
	public int getLocalPort() {
		return connection.getLocalPort();
	}
	
	
	
	public String getReadableIPAddress() {
		return connection.getInetAddress().getHostAddress();
	}
}