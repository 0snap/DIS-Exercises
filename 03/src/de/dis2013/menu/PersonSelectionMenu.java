package de.dis2013.menu;

import java.util.Iterator;
import java.util.Set;

import de.dis2013.data.Person;

/**
 * Ein kleines Menü, dass alle Personen aus einem Set zur Auswahl anzeigt
 */
public class PersonSelectionMenu extends Menu {
	public static final int BACK = -1;
	
	public PersonSelectionMenu(String title, Set<Person> personen) {
		super(title);
		
		Iterator<Person> it = personen.iterator();
		while(it.hasNext()) {
			Person p = it.next();
			addEntry(p.getFirstName()+" "+p.getName(), p.getId());
		}
		addEntry("Zurück", BACK);
	}
}
