package de.dis2013.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
	/**
	 * Compares two objects. A and B may be null
	 * @param a First object
	 * @param b Second object
	 * @return True if the objects are both null or if they are equal
	 */
	public static boolean compareObjects(Object a, Object b) {
		if(a == null) {
			if(b == null)
				return true; //a and b both null
			
			return false; //a null, b not null
		}
		
		if(b == null)
			return false; //a not null, b null
		
		//neither a nor b null
		return a.equals(b);
	}
	
	/**
	 * Gibt ein Datum als String im Format dd.MM.yyyy zurück
	 * @param date Das zu konvertierende Datum
	 * @return Datum als String
	 */
	public static String dateToString(Date date) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
		StringBuilder sb = new StringBuilder(dateFormatter.format(date));
		return sb.toString();
	}
}
