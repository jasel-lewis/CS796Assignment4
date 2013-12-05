/**
 * 
 */
package com.jasel.classes.cs796.assignment4.view;

import java.awt.Color;

/**
 * @author Jasel
 */
public enum MessageType {
	NORMAL,			// Normal messages
	INFORMATIONAL,  // Informational messages
	WARNING,		// Warning messages
	ERROR;			// Error messages
	
	
	public Color getColor() {
		Color color = null;
		
		switch (this) {
		case NORMAL:
			color = Color.BLACK;
			break;
		case INFORMATIONAL:
			color = Color.BLUE;
			break;
		case WARNING:
			color = Color.YELLOW;
			break;
		case ERROR:
			color = Color.RED;
			break;
		default:
			color = Color.DARK_GRAY;
			break;
		}
		
		return color;
	}
	
	
	
	@Override
	public String toString() {
		String name = name();
		
		return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}
}