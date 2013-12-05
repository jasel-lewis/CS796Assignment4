/**
 * 
 */
package com.jasel.classes.cs796.assignment4.model;

/**
 * @author Jasel
 */
public enum ClientType {
	NORMAL, URGENT;
	
	@Override
	public String toString() {
		String name = name();
		
		return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}
}