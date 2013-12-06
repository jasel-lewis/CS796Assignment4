package com.jasel.classes.cs796.assignment4.view;

import java.awt.Color;

/**
 * @author Jasel
 */
public enum MessageType {
	NORMAL,			// Normal messages
	SUBDUED,		// Subdued messages
	INFORMATIONAL,  // Informational messages
	WARNING,		// Warning messages
	ERROR;			// Error messages
	
	
	/**
	 * Return an associated Color for each MessageType
	 * @return
	 */
	public Color getColor() {
		Color color = null;
		
		switch (this) {
			case INFORMATIONAL:
				color = Color.BLUE;
				break;
			case WARNING:
				color = Color.ORANGE;
				break;
			case ERROR:
				color = Color.RED;
				break;
			case SUBDUED:
				color = Color.LIGHT_GRAY;
				break;
			case NORMAL:
			default:
				color = Color.BLACK;
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