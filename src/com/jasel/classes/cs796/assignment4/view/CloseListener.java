/**
 * 
 */
package com.jasel.classes.cs796.assignment4.view;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class will help you take control of the frame closing process by
 * providing a WindowListener with some common features that might be used
 * when closing an application. That is you can:
 * 	<ul>
 * 		<li>display an application closing confirmation message which will
 * 			give the user a chance to cancel the close request
 * 		<li>invoke a provide Action. This action might be used to save the
 * 			state of the application for the next time it is used.
 * 	</ul>
 * 
 * This class will invoke the setDefaultCloseOperation(...) method with an
 * appropriate value. The default will be set to EXIT_ON_CLOSE. The default
 * can be changed by invoking the setDisoseOnClose() method with "true", in
 * which case DISPOSE_ON_CLOSE will be used. This value will be set to
 * DO_NOTHING_ON_CLOSE if the user does not confirm the application closing
 * message. Finally, the close action is free to set this property to any
 * value.
 * 
 * @author Rob Camick
 * {@link http://tips4java.wordpress.com/2009/05/01/closing-an-application/}
 * {@link http://www.camick.com/java/source/CloseListener.java}
 */
public class CloseListener extends WindowAdapter {
	private String message;
	private String title;
	private Action closeAction;
	private boolean disposeOnClose = false;

	/**
	 *  Display a confirmation message upon closing the application
	 *
	 *  @param title    the text used for the title bar of the dialog
	 *  @param message  the confirmation message
	 */
	public CloseListener(String title, String message) {
		this(title, message, null);
	}

	
	
	/**
	 *  Specify an Action to be invoked upon closing the application.
	 *
	 *  @param closeAction  the Action to be invoked upon closing the application
	 */
	public CloseListener(Action closeAction) {
		this(null, null, closeAction);
	}

	
	
	/**
	 *  Display a confirmation message upon closing the application and
	 *	and invoke a closing action.
	 *
	 *  @param title        the text used for the title bar of the dialog
	 *  @param message      the confirmation message
	 *  @param closeAction  the Action to be invoked upon closing the application
	 */
	public CloseListener(String title, String message, Action closeAction) {
		this.title = title;
		this.message = message;
		this.closeAction = closeAction;
	}

	
	
	/**
	 *  Set the default close operation to be dispose on close. Typically would
	 *  be used in a multi-frame application. This way only when the last frame
	 *  is closed will the JVM be shut down.
	 *
	 *  @param disposeOnClose  true for DISPOSE_ON_CLOSE, false for EXIT_ON_CLOSE
	 */
	public void setDisposeOnClose(boolean disposeOnClose) {
		this.disposeOnClose = disposeOnClose;
	}

	
	
	@Override
	public void windowClosing(WindowEvent e) {
		JFrame frame = (JFrame)e.getComponent();

		//  Confirm with the user to close the frame
		if (!(confirmWindowClosing(frame))) {
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			
			return;
		}

		//  Set the close option. Setting it here allows the close action to
		//  prevent the window from closing by resetting the close option to
		//  DO_NOTHING_ON_CLOSE.
		if (disposeOnClose) {
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		} else {
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		//  Confirmation received, so do the exit processing
		if (closeAction != null) {
			ActionEvent ae = new ActionEvent(frame, ActionEvent.ACTION_PERFORMED, "");
			closeAction.actionPerformed(ae);
		}
	}

	
	
	private boolean confirmWindowClosing(JFrame frame) {
		if (message == null) {
			return true;
		}

		int result = JOptionPane.showConfirmDialog(
			frame,
			message,
			title,
			JOptionPane.YES_NO_OPTION);

		return (result == JOptionPane.YES_OPTION) ? true : false;
	}
}