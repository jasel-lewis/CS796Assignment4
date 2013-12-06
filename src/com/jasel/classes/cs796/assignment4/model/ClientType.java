package com.jasel.classes.cs796.assignment4.model;

/**
 * @author Jasel
 */
public enum ClientType {
	NORMAL,			// Client will be contacted back by the server
	URGENT,			// Client will use initial established connection with the server
	UNSPECIFIED;	// Client is undetermined as of yet
	
	@Override
	public String toString() {
		String name = name();
		
		return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}
}