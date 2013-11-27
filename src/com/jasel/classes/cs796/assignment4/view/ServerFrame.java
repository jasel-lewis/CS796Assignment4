/**
 * http://docs.oracle.com/javase/tutorial/uiswing/components/menu.html
 */

package com.jasel.classes.cs796.assignment4.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.KeyStroke;

import com.jasel.classes.cs796.assignment4.controller.ServerController;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.BoxLayout;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;

public class ServerFrame extends JFrame {
	private static final long serialVersionUID = -3579124248456596891L;
	private JPanel contentPane;
	private ServerController controller;
	

	/**
	 * Create the frame.
	 */
	public ServerFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic('F');
		menuBar.add(menuFile);
		
		JMenuItem menuItemExit = new JMenuItem("Exit");
		menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		menuFile.add(menuItemExit);
		
		JMenu menuAction = new JMenu("Action");
		menuAction.setMnemonic('A');
		menuBar.add(menuAction);
		
		JMenuItem menuItemStartListening = new JMenuItem("Start Listening");
		menuItemStartListening.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		menuAction.add(menuItemStartListening);
		
		JMenuItem menuItemStopListening = new JMenuItem("Stop Listening");
		menuItemStopListening.setEnabled(false);
		menuItemStopListening.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		menuAction.add(menuItemStopListening);
		
		JSeparator separator = new JSeparator();
		menuAction.add(separator);
		
		JMenuItem menuItemClearLogPane = new JMenuItem("Clear Log Pane");
		menuItemClearLogPane.setEnabled(false);
		menuItemClearLogPane.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		menuAction.add(menuItemClearLogPane);
		
		Component menuHorizontalGlue = Box.createHorizontalGlue();
		menuBar.add(menuHorizontalGlue);
		
		JMenu menuHelp = new JMenu("Help");
		menuHelp.setMnemonic('H');
		menuBar.add(menuHelp);
		
		JMenuItem menuItemAbout = new JMenuItem("About");
		menuHelp.add(menuItemAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.add(controlPanel, BorderLayout.SOUTH);
		
		JButton buttonClearLog = new JButton("Clear Log");
		buttonClearLog.setEnabled(false);
		
		Component controlHorizontalGlue = Box.createHorizontalGlue();
		
		JSpinner portSpinner = new JSpinner();
		portSpinner.setToolTipText("Port range is from 1024 to 65535, inclusive");
		portSpinner.setModel(new SpinnerNumberModel(8008, 1024, 65535, 1));
		
		JButton buttonListen = new JButton("Start Listening");
		buttonListen.setMnemonic('S');
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		controlPanel.add(buttonClearLog);
		controlPanel.add(controlHorizontalGlue);
		
		JLabel labelPort = new JLabel("Port:");
		controlPanel.add(labelPort);
		controlPanel.add(portSpinner);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		controlPanel.add(horizontalStrut);
		controlPanel.add(buttonListen);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JList list = new JList(controller.getModel());
		list.setEnabled(false);
		list.setVisibleRowCount(-1);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		splitPane.setLeftComponent(list);
		
		JTextArea textArea = new JTextArea();
		textArea.setTabSize(4);
		textArea.setEditable(false);
		splitPane.setRightComponent(textArea);
	}


	public void setController(ServerController controller) {
		this.controller = controller;
	}
}