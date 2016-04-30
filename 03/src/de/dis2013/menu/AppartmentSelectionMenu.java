package de.dis2013.menu;

import java.util.Iterator;
import java.util.Set;

import de.dis2013.data.Apartment;

/**
 * Ein kleines Menü, dass alle Wohnungen aus einem Set zur Auswahl anzeigt
 */
public class AppartmentSelectionMenu extends Menu {
	public static final int BACK = -1;
	
	public AppartmentSelectionMenu(String title, Set<Apartment> wohnungen) {
		super(title);
		
		Iterator<Apartment> it = wohnungen.iterator();
		while(it.hasNext()) {
			Apartment w = it.next();
			addEntry(w.getStreet()+" "+w.getStreetNumber()+", "+w.getPostalCode()+" "+w.getCity(), w.getId());
		}
		addEntry("Zurück", BACK);
	}
}
