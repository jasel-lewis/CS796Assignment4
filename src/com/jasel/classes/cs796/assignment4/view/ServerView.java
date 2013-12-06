package com.jasel.classes.cs796.assignment4.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
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
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.jasel.classes.cs796.assignment4.controller.ServerController;
import com.jasel.classes.cs796.assignment4.model.ConnectionTableModel;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;


public class ServerView extends JFrame implements TableModelListener {
	private static final long serialVersionUID = -3579124248456596891L;
	private static final int VIEWABLEROWS = 6;
	
	private int defaultPort = 0;
	private boolean clearLogEnabled = false;
	
	private JPanel contentPane;
	private JButton clearLogButton, listenButton;
	private JSpinner portSpinner;
	private JTextPane log;
	private ServerController controller;
	private ConnectionTableModel tableModel;
	private JTable table;
	private JMenuItem menuItemClearLog;
	private JMenuItem menuItemStartListening;
	private JMenuItem menuItemStopListening;
	private StyledDocument document;
	

	/**
	 * Create the frame.
	 */
	public ServerView(int defaultPort) {
		this.defaultPort = defaultPort;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 551, 400);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic('F');
		menuBar.add(menuFile);
		
		JMenuItem menuItemExit = new JMenuItem("Exit");
		menuItemExit.setMnemonic('x');
		menuItemExit.setMnemonic(KeyEvent.VK_X);
		menuItemExit.addActionListener(new ExitClick());
		menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		menuFile.add(menuItemExit);
		
		JMenu menuAction = new JMenu("Action");
		menuAction.setMnemonic('A');
		menuBar.add(menuAction);
		
		menuItemStartListening = new JMenuItem("Start Listening");
		menuItemStartListening.setMnemonic('s');
		menuItemStartListening.setMnemonic(KeyEvent.VK_S);
		menuItemStartListening.addActionListener(new ListenItemClick());
		menuItemStartListening.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		menuAction.add(menuItemStartListening);
		
		menuItemStopListening = new JMenuItem("Stop Listening");
		menuItemStopListening.setMnemonic('p');
		menuItemStopListening.setMnemonic(KeyEvent.VK_P);
		menuItemStopListening.addActionListener(new ListenItemClick());
		menuItemStopListening.setEnabled(false);
		menuItemStopListening.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		menuAction.add(menuItemStopListening);
		
		JSeparator separator = new JSeparator();
		menuAction.add(separator);
		
		menuItemClearLog = new JMenuItem("Clear Log");
		menuItemClearLog.setMnemonic('l');
		menuItemClearLog.setMnemonic(KeyEvent.VK_L);
		menuItemClearLog.addActionListener(new ClearLogClick());
		menuItemClearLog.setEnabled(false);
		menuItemClearLog.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		menuAction.add(menuItemClearLog);
		
		Component menuHorizontalGlue = Box.createHorizontalGlue();
		menuBar.add(menuHorizontalGlue);
		
		JMenu menuHelp = new JMenu("Help");
		menuHelp.setMnemonic('H');
		menuBar.add(menuHelp);
		
		JMenuItem menuItemAbout = new JMenuItem("About");
		menuItemAbout.setMnemonic('a');
		menuItemAbout.setMnemonic(KeyEvent.VK_A);
		menuHelp.add(menuItemAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.add(controlPanel, BorderLayout.SOUTH);
		
		clearLogButton = new JButton("Clear Log");
		clearLogButton.addActionListener(new ClearLogClick());
		clearLogButton.setEnabled(false);
		
		Component controlHorizontalGlue = Box.createHorizontalGlue();
		
		portSpinner = new JSpinner();
		portSpinner.setToolTipText("Port range is from 1024 to 65535, inclusive");
		portSpinner.setModel(new SpinnerNumberModel(defaultPort, 1024, 65535, 1));
		portSpinner.setEditor(new JSpinner.NumberEditor(portSpinner, "#"));
		
		listenButton = new JButton("Start Listening");
		listenButton.addActionListener(new ListenItemClick());
		
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		controlPanel.add(clearLogButton);
		controlPanel.add(controlHorizontalGlue);
		
		JLabel labelPort = new JLabel("Port:");
		controlPanel.add(labelPort);
		controlPanel.add(portSpinner);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		controlPanel.add(horizontalStrut);
		controlPanel.add(listenButton);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JScrollPane tableScrollPane = new JScrollPane();
		splitPane.setLeftComponent(tableScrollPane);
		
		JScrollPane logScrollPane = new JScrollPane();
		splitPane.setRightComponent(logScrollPane);
		
		log = new JTextPane();
		log.setEditable(false);
		
		document = log.getStyledDocument();
		addStylesToDocument(document);
		
		logScrollPane.setViewportView(log);
		
		tableModel = new ConnectionTableModel();
		table = new JTable(tableModel);
		table.setFillsViewportHeight(true);
		table.getModel().addTableModelListener(this);
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(false);
		table.setShowGrid(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setEnabled(false);
		
		tableScrollPane.setViewportView(table);
	}
	
	
	
	private void addStylesToDocument(StyledDocument document) {
		Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		Style style = null;
		
		for (MessageType messageType : MessageType.values()) {
			style = document.addStyle(messageType.name(), defaultStyle);
			StyleConstants.setForeground(style, messageType.getColor());
		}
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
	
	
	
	private class ListenItemClick implements ActionListener {
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
			getController().handleClearLogClick();
		}
	}
	
	
	
	public void clearLog() {
		log.setText(null);
		clearLogEnabled = false;
		clearLogButton.setEnabled(false);
		menuItemClearLog.setEnabled(false);
	}
	
	
	
	public void writeToLog(String message, MessageType messageType) {
		try {
			document.insertString(document.getLength(), message, document.getStyle(messageType.name()));
		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert initial message into log");
		}
		
		if (!clearLogEnabled) {
			clearLogButton.setEnabled(true);
			menuItemClearLog.setEnabled(true);
			clearLogEnabled = true;
		}
	}
	
	
	
	public void configureForListeningState(boolean listening) {
		if (listening) {
			listenButton.setText("Stop Listening");
			menuItemStopListening.setEnabled(true);
			menuItemStartListening.setEnabled(false);
			portSpinner.setEnabled(false);
		} else {
			listenButton.setText("Start Listening");
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