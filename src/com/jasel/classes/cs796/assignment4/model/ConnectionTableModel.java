package com.jasel.classes.cs796.assignment4.model;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * Using a Vector as its structural modification methods are already synchronized.  This
 * saves the trouble of having to implement synchronization on an ArrayList or similar.
 * 
 * @author Jasel
 */
public class ConnectionTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 4063686823137272854L;
	private String[] columnNames = {"Client IP Address", "Client Port", "Client Type"};
	private Vector<Connection> connections = new Vector<Connection>();
	
	public Vector<Connection> getConnections() {
		return connections;
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
		return connections.size();
	}

	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int row, int column) {
		Connection connection = connections.get(row);
		Object value = null;
		
		switch (column) {
			case 0:
				value = connection.getIPv4();
				break;
			case 1:
				value = connection.getPort();
				break;
			case 2:
				value = connection.getType();
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
	
	
	
	public void addConnection(Connection connection) {
		connections.add(connection);
		fireTableDataChanged();
	}
	
	
	
	public void removeConnection(Connection connection) {
		connections.remove(connection);
		fireTableDataChanged();
	}
	
	
	
	public void clear() {
		connections.clear();
		fireTableDataChanged();
	}
}