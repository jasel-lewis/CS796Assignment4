package com.jasel.classes.cs796.assignment4.model;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * Using a Vector as its structural modification methods are already synchronized.  This
 * saves the trouble of having to implement synchronization on an ArrayList or similar.
 * 
 * @author Jasel
 */
public class ClientTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 4063686823137272854L;
	private String[] columnNames = {"Client IP Address", "Client Port", "Client Type"};
	private Vector<Client> clients = new Vector<Client>();
	
	public Vector<Client> getClients() {
		return clients;
	}

	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	
	
	public String getColumnName(int index) {
		return columnNames[index];
	}

	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return clients.size();
	}

	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int row, int column) {
		Client client = clients.get(row);
		Object value = null;
		
		switch (column) {
			case 0:
				value = client.getReadableIPAddress();
				break;
			case 1:
				value = client.getRemotePort();
				break;
			case 2:
				value = client.getClientType();
				break;
			default:
				value = new String("error");
				break;
		}
		
		return value;
	}
	
	
	
	public Class<?> getColumnClass(int column) {
		return getValueAt(0, column).getClass();
	}
	
	
	
	public void addClient(Client client) {
		clients.add(client);
		fireTableDataChanged();
	}
	
	
	
	public void removeClient(Client client) {
		clients.remove(client);
		fireTableDataChanged();
	}
	
	
	
	public void updateClientType(Client client, ClientType clientType) {
		clients.get(clients.indexOf(client)).setClientType(clientType);
		fireTableDataChanged();
	}
	
	
	
	public void clear() {
		clients.clear();
		fireTableDataChanged();
	}
}