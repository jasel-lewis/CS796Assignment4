/**
 * @author Jasel
 */
package com.jasel.classes.cs796.assignment4;

import java.awt.EventQueue;

import com.jasel.classes.cs796.assignment4.controller.ServerController;
import com.jasel.classes.cs796.assignment4.view.CloseListener;
import com.jasel.classes.cs796.assignment4.view.ServerView;

public class unoserver {
	protected static final int DEFAULTPORT = 8008;

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
					ServerView view = new ServerView(DEFAULTPORT);
					ServerController controller = new ServerController(view.getTableModel().getConnections(), view);
					
					view.setController(controller);
					view.addWindowListener(new CloseListener("Exit UNOServer", "Are you sure you wish to exit UNOServer?"));
					//view.pack();
					view.setViewportHeight();
					view.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	private static void printUsage(int exitCode) {
		System.out.println("UNOserver - Urgent/Normal On-demand Server");
		System.out.println("Usage: unoserver\n");
		
		System.exit(exitCode);
	}
}