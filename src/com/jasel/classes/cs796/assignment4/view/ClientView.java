package com.jasel.classes.cs796.assignment4.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.DefaultComboBoxModel;

import com.jasel.classes.cs796.assignment4.controller.ClientController;
import com.jasel.classes.cs796.assignment4.model.ClientType;

import javax.swing.SpinnerNumberModel;
import javax.swing.JTextPane;

public class ClientView extends JFrame {
	private static final long serialVersionUID = -8465970805529299589L;
	
	private int defaultPort = 0;
	private boolean clearLogEnabled = false;
	private ClientController controller = null;
	
	private JTextField ipAddressTextField;
	private JSpinner portSpinner;
	private JComboBox<ClientType> clientTypeCombo;
	private JButton connectButton;
	private JTextPane log;
	private JButton clearLogButton;
	private JTextField messageField;
	private JButton sendButton;
	private StyleContext styleContext;


	/**
	 * Create the frame.
	 */
	public ClientView(int defaultPort) {
		this.defaultPort = defaultPort;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 370);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel connectControlPanel = new JPanel();
		connectControlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.add(connectControlPanel, BorderLayout.NORTH);
		connectControlPanel.setLayout(
			new FormLayout(
				new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("right:default"),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.GLUE_COLSPEC,
					ColumnSpec.decode("15dlu"),
					ColumnSpec.decode("right:default"),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					ColumnSpec.decode("left:max(50dlu;pref)"),
					FormFactory.RELATED_GAP_COLSPEC
				},
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.NARROW_LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}
			)
		);
		
		JLabel ipAddressLabel = new JLabel("IPv4 Address:");
		connectControlPanel.add(ipAddressLabel, "2, 1, right, default");
		
		ipAddressTextField = new JTextField();
		connectControlPanel.add(ipAddressTextField, "4, 1, fill, default");
		ipAddressTextField.setColumns(10);
		
		JLabel clientTypeLabel = new JLabel("Client Type:");
		connectControlPanel.add(clientTypeLabel, "6, 1, right, default");
		
		clientTypeCombo = new JComboBox<ClientType>();
		clientTypeCombo.setModel(new DefaultComboBoxModel<ClientType>(ClientType.values()));
		clientTypeCombo.setSelectedIndex(0);
		clientTypeCombo.setMaximumRowCount(2);
		connectControlPanel.add(clientTypeCombo, "8, 1, fill, default");
		
		JLabel portLabel = new JLabel("Port:");
		connectControlPanel.add(portLabel, "2, 3, right, default");
		
		portSpinner = new JSpinner();
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
		messageField.setEnabled(false);
		messageField.addActionListener(new SendAction());
		messageField.setColumns(10);  // TODO: is this line needed??
		inputControlPanel.add(messageField, BorderLayout.CENTER);
		
		sendButton = new JButton("Send");
		sendButton.addActionListener(new SendAction());
		sendButton.setEnabled(false);
		inputControlPanel.add(sendButton, BorderLayout.EAST);
		
		
		JPanel logControlPanel = new JPanel();
		logControlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		jPanel.add(logControlPanel, BorderLayout.NORTH);
		logControlPanel.setLayout(new BorderLayout(0, 0));
		
		clearLogButton = new JButton("Clear Log");
		clearLogButton.addActionListener(new ClearLogClick());
		clearLogButton.setEnabled(false);
		logControlPanel.add(clearLogButton, BorderLayout.WEST);
		
		JScrollPane logScrollPane = new JScrollPane();
		contentPane.add(logScrollPane, BorderLayout.CENTER);
		
		log = new JTextPane();
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
			
			getController().handleConnectClick(
					ipAddressTextField.getText(),
					port,
					(ClientType)clientTypeCombo.getSelectedItem()
			);
		}
	}
	
	
	
	private class ClearLogClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			getController().handleClearLogClick();
		}
	}
	
	
	
	private class SendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			getController().handleSendClick(messageField.getText());
		}
	}
	
	
	
	public void clearLog() {
		log.setText(null);
		clearLogEnabled = false;
		clearLogButton.setEnabled(false);
	}
	
	
	
	public void clearMessage() {
		messageField.setText("");
	}
	
	
	
//	public void writeToLog(String text) {
//		log.append(text);
//		
//		if (!clearLogEnabled) {
//			clearLogButton.setEnabled(true);
//		}
//	}
	
	
	
	public void writeToLog(String message, MessageType messageType) {
		styleContext = StyleContext.getDefaultStyleContext();
		AttributeSet attribSet = styleContext.addAttribute(
				SimpleAttributeSet.EMPTY,
				StyleConstants.Foreground,
				messageType.getColor()
		);
		
		log.setCaretPosition(log.getDocument().getLength());
		log.setCharacterAttributes(attribSet, false);
		log.replaceSelection(message);
		
		if (!clearLogEnabled) {
			clearLogButton.setEnabled(true);
		}
	}
	
	
	
	public void configureForConnectedState(boolean connected) {
		connectButton.setEnabled(true);
		
		if (connected) {
			connectButton.setText("Disconnect");

			messageField.setEnabled(true);
			sendButton.setEnabled(true);
			
			ipAddressTextField.setEnabled(false);
			portSpinner.setEnabled(false);
			clientTypeCombo.setEnabled(false);
		} else {
			connectButton.setText("Connect");
			
			messageField.setEnabled(false);
			sendButton.setEnabled(false);
			
			ipAddressTextField.setEnabled(true);
			portSpinner.setEnabled(true);
			clientTypeCombo.setEnabled(true);
		}
	}
	
	
	
	public void configureForConnectingState(boolean connecting) {
		if (connecting) {
			connectButton.setEnabled(false);
			
			connectButton.setText("Connecting...");
			
			ipAddressTextField.setEnabled(false);
			portSpinner.setEnabled(false);
			clientTypeCombo.setEnabled(false);
		} else {
			configureForConnectedState(false);
		}
	}
}