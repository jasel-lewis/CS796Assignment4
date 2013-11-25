/**
 * 
 */
package com.jasel.classes.cs796.assignment4;

/**
 * @author Jasel
 *
 */
public class unoserver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port;
		
		if (args.length == 1) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException nfe) {
				handlePortError();
			}
		} else {
			printUsage(1);
			
		}
	}
	
	
	
	private static void printUsage(int exitCode) {
		System.out.println("UNOserver - Urgent/Normal On-demand Server");
		System.out.println("Usage: unoserver <port>");
		System.out.println("   - <port> The port on which to run the server (in " +
				"the range 1024-65535)");
		
		System.exit(exitCode);
	}
	
	
	
	private static void handlePortError() {
		System.err.println("The sole argument <port> must be an integer in the " +
				"range 1024 to 65535");
		System.exit(1);
	}
}