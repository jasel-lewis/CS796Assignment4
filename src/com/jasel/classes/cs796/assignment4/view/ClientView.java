package com.jasel.classes.cs796.assignment4.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Component;
import javax.swing.Box;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.DefaultComboBoxModel;

import com.jasel.classes.cs796.assignment4.controller.ServerController;
import com.jasel.classes.cs796.assignment4.model.ClientType;

import javax.swing.SpinnerNumberModel;

public class ClientView extends JFrame {
	private static final long serialVersionUID = -8465970805529299589L;
	
	private int defaultPort = 0;
	private boolean clearLogEnabled = false;
	private ClientController clientController = null;
	
	private JPanel contentPane;
	private JTextField messageField;
	private JTextField ipAddressTextField;
	private JTextArea log;
	private JButton connectButton;
	private JButton clearLogButton;
	private JButton sendButton;


	/**
	 * Create the frame.
	 */
	public ClientView(int defaultPort) {
		this.defaultPort = defaultPort;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel connectControlPanel = new JPanel();
		connectControlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.add(connectControlPanel, BorderLayout.NORTH);
		connectControlPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:default"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.GLUE_COLSPEC,
				ColumnSpec.decode("15dlu"),
				ColumnSpec.decode("right:default"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("left:max(50dlu;pref)"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel ipAddressLabel = new JLabel("IP Address:");
		connectControlPanel.add(ipAddressLabel, "2, 1, right, default");
		
		ipAddressTextField = new JTextField();
		connectControlPanel.add(ipAddressTextField, "4, 1, fill, default");
		ipAddressTextField.setColumns(10);
		
		JLabel clientTypeLabel = new JLabel("Client Type:");
		connectControlPanel.add(clientTypeLabel, "6, 1, right, default");
		
		JComboBox<ClientType> clientTypeCombo = new JComboBox<ClientType>();
		clientTypeCombo.setModel(new DefaultComboBoxModel<ClientType>(ClientType.values()));
		clientTypeCombo.setSelectedIndex(0);
		clientTypeCombo.setMaximumRowCount(2);
		connectControlPanel.add(clientTypeCombo, "8, 1, fill, default");
		
		JLabel portLabel = new JLabel("Port:");
		connectControlPanel.add(portLabel, "2, 3, right, default");
		
		JSpinner portSpinner = new JSpinner();
		portSpinner.setModel(new SpinnerNumberModel(defaultPort, 1024, 65535, 1));
		portSpinner.setToolTipText("Port range is from 1024 to 65535, inclusive");
		portSpinner.setEditor(new JSpinner.NumberEditor(portSpinner, "#"));
		((JSpinner.DefaultEditor)portSpinner.getEditor()).getTextField().setColumns(5);
		connectControlPanel.add(portSpinner, "4, 3");
		
		connectButton = new JButton("Connect");
		connectButton.addActionListener(new ConnectClick());
		connectControlPanel.add(connectButton, "8, 3");
		
		JPanel jPanel = new JPanel();
		jPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		contentPane.add(jPanel, BorderLayout.SOUTH);
		jPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel inputControlPanel = new JPanel();
		inputControlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		jPanel.add(inputControlPanel, BorderLayout.SOUTH);
		inputControlPanel.setLayout(new BorderLayout(10, 0));
		
		messageField = new JTextField();
		inputControlPanel.add(messageField, BorderLayout.CENTER);
		messageField.setColumns(10);
		
		sendButton = new JButton("Send");
		inputControlPanel.add(sendButton, BorderLayout.EAST);
		sendButton.addActionListener(new SendClick());
		sendButton.setEnabled(false);
		
		JPanel logControlPanel = new JPanel();
		logControlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		jPanel.add(logControlPanel, BorderLayout.NORTH);
		logControlPanel.setLayout(new BorderLayout(0, 0));
		
		clearLogButton = new JButton("Clear Log");
		clearLogButton.setEnabled(false);
		logControlPanel.add(clearLogButton, BorderLayout.WEST);
		
		JScrollPane logScrollPane = new JScrollPane();
		contentPane.add(logScrollPane, BorderLayout.CENTER);
		
		log = new JTextArea();
		log.setEditable(false);
		logScrollPane.setViewportView(log);
	}
	
	
	
	public void setController(ClientController controller) {
		this.controller = controller;
	}
	
	
	
	public ClientController getController() {
		return controller;
	}
	
	
	
	private class ConnectClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			getController().handleConnectClick();
		}
	}
	
	
	
	private class SendClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			getController().handleConnectClick();
		}
	}
	
	
	
	public void clearLog() {
		log.setText(null);
		clearLogEnabled = false;
		clearLogButton.setEnabled(false);
	}
	
	
	
	public void appendToLog(String text) {
		log.append(text);
		
		if (!clearLogEnabled) {
			clearLogButton.setEnabled(true);
		}
	}
}