package de.dis2013.menu;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.dis2013.data.House;

/**
 * Ein kleines Menü, dass alle Häuser aus einem Set zur Auswahl anzeigt
 */
public class HouseSelectionMenu extends Menu {
	public static final int BACK = -1;
	
	public HouseSelectionMenu(String title, List<House> houses) {
		super(title);

		for (House house : houses) {
			addEntry(house.getStreet()+" "+house.getStreetNumber()+", "+house.getPostalCode()+" "+house.getCity(), house.getId());
		}
		addEntry("Back", BACK);
	}
}
