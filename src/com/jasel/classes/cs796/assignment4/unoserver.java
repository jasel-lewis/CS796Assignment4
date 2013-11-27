/**
 * 
 */
package com.jasel.classes.cs796.assignment4;

import java.awt.EventQueue;

import com.jasel.classes.cs796.assignment4.controller.ServerController;
import com.jasel.classes.cs796.assignment4.model.ConnectionListModel;
import com.jasel.classes.cs796.assignment4.view.ServerFrame;

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
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectionListModel clm = new ConnectionListModel();
					ServerFrame view = new ServerFrame();
					ServerController controller = new ServerController(clm, view);
					
					view.setController(controller);
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