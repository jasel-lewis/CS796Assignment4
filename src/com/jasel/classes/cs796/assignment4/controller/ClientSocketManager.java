/**
 * 
 */
package com.jasel.classes.cs796.assignment4.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import com.jasel.classes.cs796.assignment4.model.Connection;

/**
 * @author Jasel
 *
 */
public class ClientSocketManager implements Runnable {
	private Connection connection = null;
	private volatile boolean running = false;
	
	public ClientSocketManager(Vector<Connection> connections, Connection connection) {
		this.connection = connection;
		connections.add(connection);
	}
	
	
	
	@Override
	public void run() {
		BufferedReader br = null;
		String input = null;
		
		running = true;

		while(running) {
			try {
				br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
				try {
					input = br.readLine();
		
					if (input != null) {
						// Echo back the string
						writeToConnection(input, false);
					} else {
						break;
					}
				} catch (IOException ioe) {
					if (!connection.isInputShutdown()) {
						System.err.println("Could not call readLine() on the Socket's BufferedReader.");
						System.exit(-1);
					}
				}
			} catch (IOException ioe) {
				System.err.println(ioe);
				System.err.println("Could not open a BufferedReader for the Socket: " + connection.toString() + ".");
			}
		}
	}
	
	
	
	/**
	 * Write to the Socket associated with this thread.  If isServerMessage is
	 * TRUE, the message is specially formatted as an alert to the client from
	 * the server (vice a simple echo-back).
	 * 
	 * @param str
	 * @param isServerMessage
	 */
	public void writeToConnection(String str, boolean isServerMessage) {
		PrintWriter pw = null;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		StringBuilder sb = new StringBuilder();

		if (!isServerMessage) {
			sb.append(" (");
			sb.append(sdf.format(date));
			sb.append("):  ");
		} else {
			sb.append("echoback> ");
		}

		sb.append(str);

		try {
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())), true);

			pw.println(sb);

			// Closing pw here after we're done using it seems like the right thing to do. The messed
			// up thing is that if we close it, we kill the connection and our next call to br.readLine()
			// fails in the while loop up above in run() (which we return to).
			// pw.close();
		} catch (IOException ioe) {
			System.err.println(ioe);
			System.err.println("Could not open a PrintWriter to the socket: " + connection.toString() + ".");
		}
	}
	
	
	
	/**
	 * Prevent future echo-back with the socket, indicate that the server has
	 * chosen to terminate the connection, and close the socket
	 */
	public void terminate() {
		running = false;
		
		if (connection != null) {
			writeToConnection("Server has terminated the connection.  Goodbye...", true);
			try {
				connection.close();
			} catch (IOException e) {
				System.err.println("Could not close the socket.  Runaway socket.");
				e.printStackTrace();
			}
		}
	}
}