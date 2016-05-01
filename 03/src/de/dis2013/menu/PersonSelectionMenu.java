package de.dis2013.menu;

import java.util.List;

import de.dis2013.data.Person;

public class PersonSelectionMenu extends Menu {
	public static final int BACK = -1;
	
	public PersonSelectionMenu(String title, List<Person> persons) {
		super(title);

		for(Person person : persons) {
			addEntry(person.getFirstName() + " " + person.getName(), person.getId());
		}
		addEntry("Back", BACK);
	}
}
