package de.dis2013.menu;

import java.util.Iterator;
import java.util.Set;

import de.dis2013.data.House;

/**
 * Ein kleines Menü, dass alle Häuser aus einem Set zur Auswahl anzeigt
 */
public class HouseSelectionMenu extends Menu {
	public static final int BACK = -1;
	
	public HouseSelectionMenu(String title, Set<House> haeuser) {
		super(title);
		
		Iterator<House> it = haeuser.iterator();
		while(it.hasNext()) {
			House h = it.next();
			addEntry(h.getStreet()+" "+h.getStreetNumber()+", "+h.getPostalCode()+" "+h.getCity(), h.getId());
		}
		addEntry("Zurück", BACK);
	}
}
