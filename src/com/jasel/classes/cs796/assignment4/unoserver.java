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
		if (args.length != 0) {
			printUsage(1);
		}
	}
	
	
	
	private static void printUsage(int exitCode) {
		System.out.println("UNOserver - Urgent/Normal On-demand Server");
		System.out.println("Usage: unoserver\n");
		
		System.exit(exitCode);
	}
}