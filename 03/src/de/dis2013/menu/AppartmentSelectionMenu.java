package de.dis2013.menu;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.dis2013.data.Apartment;

/**
 * Ein kleines Menü, dass alle Wohnungen aus einem Set zur Auswahl anzeigt
 */
public class AppartmentSelectionMenu extends Menu {
	public static final int BACK = -1;
	
	public AppartmentSelectionMenu(String title, List<Apartment> apartments) {
		super(title);

		for(Apartment apartment : apartments) {
			addEntry(apartment.getStreet()+" "+apartment.getStreetNumber()+", "+apartment.getPostalCode()+" "+apartment.getCity(), apartment.getId());
		}
		addEntry("Back", BACK);
	}
}
