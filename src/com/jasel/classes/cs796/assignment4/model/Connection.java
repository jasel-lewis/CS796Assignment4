/**
 * 
 */
package com.jasel.classes.cs796.assignment4.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Jasel
 */
public class Connection {
	private Socket socket = null;
	
	
	public Connection(Socket socket) {
		this.socket = socket;
	}
	
	
	
	public Connection(InetAddress inetAddress, int port) throws IOException {
		this.socket = new Socket(inetAddress, port);
	}
	
	
	
	public InetAddress getInetAddress() {
		return socket.getInetAddress();
	}
	
	
	
	public int getRemotePort() {
		return socket.getPort();
	}
	
	
	
	public int getLocalPort() {
		return socket.getLocalPort();
	}
	
	
	
	/**
	 * Read a line of input from the other end of this Connection
	 * @return
	 */
	public String readLine() {
		BufferedReader br = null;
		String input = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	
			try {
				input = br.readLine();
			} catch (IOException ioe) {
				if (!socket.isInputShutdown()) {
					System.err.println("Could not call readLine() on the Socket's BufferedReader.");
				}
			}
		} catch (IOException ioe) {
			System.err.println(ioe);
			System.err.println("Could not open a BufferedReader for the Socket: " + socket.toString() + ".");
		}
		
		return input;
	}
	
	
	
	/**
	 * Write out to the underlying Socket
	 * 
	 * @param message
	 */
	public void write(String message) {
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			pw.println(message);
		} catch (IOException ioe) {
			System.err.println(ioe);
			System.err.println("Could not open a PrintWriter to the socket: " + socket.toString() + ".");
		}
	}



	public void close() throws IOException {
		socket.close();
	}
	
	
	
	public void shutdownInput() throws IOException {
		socket.shutdownInput();
	}
	
	
	
	public String toString() {
		return ("Connection on " + socket);
	}
}