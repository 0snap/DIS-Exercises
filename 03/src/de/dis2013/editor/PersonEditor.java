package de.dis2013.editor;

import de.dis2013.core.DataAccessService;
import de.dis2013.data.Person;
import de.dis2013.menu.Menu;
import de.dis2013.menu.PersonSelectionMenu;
import de.dis2013.util.FormUtil;

public class PersonEditor {
	private DataAccessService service;
	
	public PersonEditor(DataAccessService service) {
		this.service = service;
	}
	
	public void showPersonMenu() {
		final int NEW_PERSON = 0;
		final int EDIT_PERSON = 1;
		final int DELETE_PERSON = 2;
		final int BACK = 3;
		
		Menu personMenu = new Menu("Person Menu");
		personMenu.addEntry("New Person", NEW_PERSON);
		personMenu.addEntry("Edit Person", EDIT_PERSON);
		personMenu.addEntry("Delete Person", DELETE_PERSON);
		personMenu.addEntry("Back", BACK);
		
		while(true) {
			int response = personMenu.show();
			
			switch(response) {
				case NEW_PERSON:
					newPerson();
					break;
				case EDIT_PERSON:
					editPerson();
					break;
				case DELETE_PERSON:
					deletePerson();
					break;
				case BACK:
					return;
			}
		}
	}
	

	public void newPerson() {
		Person p = new Person();
		
		p.setFirstName(FormUtil.readString("First name"));
		p.setName(FormUtil.readString("Name"));
		p.setAddress(FormUtil.readString("Address"));
		service.persist(p);
		
		System.out.println("Person mit der ID "+p.getId()+" wurde erzeugt.");
	}
	

	public void editPerson() {
		Menu personSelectionMenu = new PersonSelectionMenu("Edit Person", service.getAllPersons());
		int id = personSelectionMenu.show();
		
		if(id != PersonSelectionMenu.BACK) {
			Person person = (Person)service.getById(Person.class, id);
			System.out.println("Edit Person "+person.getFirstName()+". Leave empty for no changes");

			String newFirstName = FormUtil.readString("First name ("+person.getFirstName()+")");
			String newName = FormUtil.readString("Name ("+person.getName()+")");
			String newAddress = FormUtil.readString("Address ("+person.getAddress()+")");
			
			if(!newFirstName.equals(""))
				person.setFirstName(newFirstName);
			if(!newName.equals(""))
				person.setName(newName);
			if(!newAddress.equals(""))
				person.setAddress(newAddress);

			service.update(person);
		}
	}

	public void deletePerson() {
		Menu personSelectionMenu = new PersonSelectionMenu("Delete Person", service.getAllPersons());
		int id = personSelectionMenu.show();
		
		if(id != PersonSelectionMenu.BACK) {
			Person person = (Person)service.getById(Person.class, id);
			service.delete(person);
		}
	}
}
