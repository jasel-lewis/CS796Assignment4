package com.jasel.classes.cs796.assignment4.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.jasel.classes.cs796.assignment4.controller.ServerController;
import com.jasel.classes.cs796.assignment4.model.ConnectionTableModel;

import javax.swing.JScrollPane;
import javax.swing.JTable;


public class ServerView extends JFrame implements TableModelListener {
	private static final long serialVersionUID = -3579124248456596891L;
	private static final int VIEWABLEROWS = 6;
	
	private JPanel contentPane;
	private JButton buttonClearLog, buttonListen;
	private JSpinner portSpinner;
	private JTextArea log;
	private ServerController controller;
	private int defaultPort = 0;
	private ConnectionTableModel tableModel;
	private JTable table;
	private JMenuItem menuItemClearLog;
	private boolean clearLogEnabled = false;
	private JMenuItem menuItemStartListening;
	private JMenuItem menuItemStopListening;
	

	/**
	 * Create the frame.
	 */
	public ServerView(int defaultPort) {
		this.defaultPort = defaultPort;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic('F');
		menuBar.add(menuFile);
		
		JMenuItem menuItemExit = new JMenuItem("Exit");
		menuItemExit.addActionListener(new ExitClick());
		menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		menuFile.add(menuItemExit);
		
		JMenu menuAction = new JMenu("Action");
		menuAction.setMnemonic('A');
		menuBar.add(menuAction);
		
		menuItemStartListening = new JMenuItem("Start Listening");
		menuItemStartListening.addActionListener(new ListenViewItemClick());
		menuItemStartListening.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		menuAction.add(menuItemStartListening);
		
		menuItemStopListening = new JMenuItem("Stop Listening");
		menuItemStopListening.addActionListener(new ListenViewItemClick());
		menuItemStopListening.setEnabled(false);
		menuItemStopListening.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		menuAction.add(menuItemStopListening);
		
		JSeparator separator = new JSeparator();
		menuAction.add(separator);
		
		menuItemClearLog = new JMenuItem("Clear Log");
		menuItemClearLog.addActionListener(new ClearLogClick());
		menuItemClearLog.setEnabled(false);
		menuItemClearLog.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		menuAction.add(menuItemClearLog);
		
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
		
		buttonClearLog = new JButton("Clear Log");
		buttonClearLog.addActionListener(new ClearLogClick());
		buttonClearLog.setEnabled(false);
		
		Component controlHorizontalGlue = Box.createHorizontalGlue();
		
		portSpinner = new JSpinner();
		portSpinner.setToolTipText("Port range is from 1024 to 65535, inclusive");
		portSpinner.setModel(new SpinnerNumberModel(defaultPort, 1024, 65535, 1));
		portSpinner.setEditor(new JSpinner.NumberEditor(portSpinner, "#"));
		
		buttonListen = new JButton("Start Listening");
		buttonListen.setMnemonic('S');
		buttonListen.addActionListener(new ListenViewItemClick());
		
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
		
		log = new JTextArea();
		log.setTabSize(4);
		log.setEditable(false);
		splitPane.setRightComponent(log);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		tableModel = new ConnectionTableModel();
		table = new JTable(tableModel);
		table.setFillsViewportHeight(true);
		table.getModel().addTableModelListener(this);
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(false);
		table.setShowGrid(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setEnabled(false);
		
		scrollPane.setViewportView(table);
	}
	
	
	
	public void setViewportHeight() {
		double width = table.getPreferredSize().getWidth();
		int headerHeight = table.getTableHeader().getHeight();
		int rowHeight = table.getRowHeight();
		Dimension size = new Dimension();

		size.setSize(width, ((VIEWABLEROWS * rowHeight) + headerHeight));
		
		table.setPreferredScrollableViewportSize(size);
	}


	
	public void setController(ServerController controller) {
		this.controller = controller;
	}
	
	
	
	public ServerController getController() {
		return controller;
	}
	
	
	
	private class ListenViewItemClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int port = defaultPort;
			
			try {
				portSpinner.commitEdit();
			} catch (ParseException pe) {
				;  // Do nothing, portSpinner will revert to previous, valid, value
			}
			
			try {
				port = Integer.parseInt(portSpinner.getValue().toString());
			} catch (NumberFormatException nfe) {
				portSpinner.setValue(port);
			}
			
			getController().handleListenViewItemClick(port);
		}
	}
	
	
	
	private class ExitClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ServerView.this.dispatchEvent(new WindowEvent(ServerView.this, WindowEvent.WINDOW_CLOSING));
		}
	}
	
	
	
	private class ClearLogClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			getController().handleClearLogButtonClick();
		}
	}
	
	
	
	public void clearLog() {
		log.setText(null);
		clearLogEnabled = false;
		buttonClearLog.setEnabled(false);
		menuItemClearLog.setEnabled(false);
	}
	
	
	
	public void appendToLog(String text) {
		log.append(text);
		
		if (!clearLogEnabled) {
			buttonClearLog.setEnabled(true);
			menuItemClearLog.setEnabled(true);
		}
	}
	
	
	
	public void configureForListeningState(boolean listening) {
		if (listening) {
			buttonListen.setText("Stop Listening");
			menuItemStopListening.setEnabled(true);
			menuItemStartListening.setEnabled(false);
			portSpinner.setEnabled(false);
		} else {
			buttonListen.setText("Start Listening");
			menuItemStartListening.setEnabled(true);
			menuItemStopListening.setEnabled(false);
			portSpinner.setEnabled(true);
		}
	}



	@Override
	public void tableChanged(TableModelEvent event) {
		;  //TODO: Do nothing? - how does the JTable get notified to update its view?
	}
	
	
	
	public ConnectionTableModel getTableModel() {
		return tableModel;
	}
}