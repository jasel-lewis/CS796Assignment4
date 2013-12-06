/**
 * 
 */
package com.jasel.classes.cs796.assignment4;

import java.awt.EventQueue;

import com.jasel.classes.cs796.assignment4.controller.ClientController;
import com.jasel.classes.cs796.assignment4.view.ClientView;
import com.jasel.classes.cs796.assignment4.view.CloseListener;

/**
 * @author Jasel
 */
public class unoclient {
	protected final static String DEFAULTADDRESS = "localhost"; 
	protected final static int DEFAULTPORT = 8008;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 0) {
			printUsage(1);
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientView view = new ClientView(DEFAULTADDRESS, DEFAULTPORT);
					ClientController controller = new ClientController(view);
					
					view.setController(controller);
					view.addWindowListener(new CloseListener("Exit UNOClient", "Are you sure you wish to exit UNOClient?"));
					//view.pack();
					view.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	private static void printUsage(int exitCode) {
		System.out.println("UNOclient - Urgent/Normal On-demand Client");
		System.out.println("Usage: unoclient\n");
		
		System.exit(exitCode);
	}
}