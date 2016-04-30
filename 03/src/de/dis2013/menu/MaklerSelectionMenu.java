package de.dis2013.menu;

import java.util.Iterator;
import java.util.Set;

import de.dis2013.data.EstateAgent;

/**
 * Ein kleines Menü, dass alle EstateAgent aus einem Set zur Auswahl anzeigt
 */
public class MaklerSelectionMenu extends Menu {
	public static final int BACK = -1;
	
	public MaklerSelectionMenu(String title, Set<EstateAgent> makler) {
		super(title);
		
		Iterator<EstateAgent> it = makler.iterator();
		while(it.hasNext()) {
			EstateAgent m = it.next();
			addEntry(m.getName(), m.getId());
		}
		addEntry("Zurück", BACK);
	}
}
