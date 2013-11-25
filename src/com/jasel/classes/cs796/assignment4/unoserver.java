/**
 * 
 */
package com.jasel.classes.cs796.assignment4;

/**
 * @author Jasel
 *
 */
public class unoserver {
	static final int PORTMIN = 1024;
	static final int PORTMAX = 65535;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 0;
		
		if (args.length == 1) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException nfe) {
				handlePortError();
			}
		} else {
			printUsage(1);
		}
		
		if ((port < PORTMIN) || (port > PORTMAX)) {
			handlePortError();
		}
	}
	
	
	
	private static void printUsage(int exitCode) {
		System.out.println("UNOserver - Urgent/Normal On-demand Server");
		System.out.println("Usage: unoserver <port>");
		System.out.println("   - <port> The port on which to run the server (in " +
				"the range " + PORTMIN + "-" + PORTMAX + ")");
		
		System.exit(exitCode);
	}
	
	
	
	private static void handlePortError() {
		System.err.println("The sole argument <port> must be an integer in the " +
				"range " + PORTMIN + "-" + PORTMAX + ")");
		System.exit(1);
	}
}