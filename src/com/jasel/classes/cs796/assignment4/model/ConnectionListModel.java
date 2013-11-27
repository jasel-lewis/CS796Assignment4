/**
 * 
 */
package com.jasel.classes.cs796.assignment4.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractListModel;

import com.jasel.classes.cs796.assignment4.view.ServerFrame;

/**
 * @author Jasel
 *
 */
public class ConnectionListModel extends AbstractListModel<Connection> {
	private static final long serialVersionUID = 7709874064164170046L;
	private SortedSet<Connection> set = null;
	private ServerFrame view = null;
	
	
	public ConnectionListModel() {
		set = new TreeSet<Connection>();
	}
	
	
	
	public void setView(ServerFrame view) {
		this.view = view;
	}
	
	
	
	@Override
	public int getSize() {
		return set.size();
	}

	
	
	@Override
	public Connection getElementAt(int index) {
		return (Connection)set.toArray()[index];
	}

	
	
	public void add(Connection connection) {
		if (set.add(connection)) {
			fireContentsChanged(this, 0, getSize());
		}
	}
	
	
	
	public void addAll(Connection connections[]) {
		Collection<Connection> collection = Arrays.asList(connections);
		
		set.addAll(collection);
		
		fireContentsChanged(this, 0, getSize());
	}
	
	
	
	public void clear() {
		set.clear();
		
		fireContentsChanged(this, 0, getSize());
	}
	
	
	
	public boolean contains(Connection connection) {
		return set.contains(connection);
	}
	
	
	
	public Connection firstConnection() {
		return set.first();
	}
	
	
	
	public Iterator<Connection> iterator() {
		return set.iterator();
	}
	
	
	
	public Connection lastConnection() {
		return set.last();
	}
	
	
	
	public boolean removeConnection(Connection connection) {
		if (set.remove(connection)) {
			fireContentsChanged(this, 0, getSize());
			
			return true;
		}
		
		return false;
	}
}