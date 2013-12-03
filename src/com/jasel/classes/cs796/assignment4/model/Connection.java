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
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jasel
 *
 */
public class Connection {
	private Socket socket = null;
	private String type = "NoTypeSpecified";
	
	public Connection(Socket socket) {
		this.socket = socket;
	}
	
	
	
	public String getIPv4() {
		return socket.getInetAddress().toString();
	}
	
	
	
	public int getPort() {
		return (socket.getPort());
	}
	
	
	
	public String getType() {
		return type;
	}



	public void setType(String string) {
		type = string;
	}
	
	
	
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
					System.exit(-1);
				}
			}
		} catch (IOException ioe) {
			System.err.println(ioe);
			System.err.println("Could not open a BufferedReader for the Socket: " + socket.toString() + ".");
		}
		
		return input;
	}
	
	
	
	/**
	 * Write to the Socket.  If isServerMessage is TRUE, the message is specially
	 * formatted as an alert to the client from the server (vice a simple echo-back).
	 * 
	 * @param str
	 * @param isServerMessage
	 */
	public void write(String str, boolean isServerMessage) {
		PrintWriter pw = null;
		StringBuilder sb = new StringBuilder();

		if (isServerMessage) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
			
			sb.append(" (");
			sb.append(sdf.format(date));
			sb.append("):  ");
		} else {
			sb.append("echoback> ");
		}

		sb.append(str);

		try {
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

			pw.println(sb);

			// Closing pw here after we're done using it seems like the right thing to do. The messed
			// up thing is that if we close it, we kill the connection and our next call to br.readLine()
			// fails in the while loop up above in run() (which we return to).
			// pw.close();
		} catch (IOException ioe) {
			System.err.println(ioe);
			System.err.println("Could not open a PrintWriter to the socket: " + socket.toString() + ".");
		}
	}



	public void close() throws IOException {
		socket.close();
	}
}