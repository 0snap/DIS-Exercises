package de.dis2013.editor;

import de.dis2013.core.ImmoService;
import de.dis2013.data.EstateAgent;
import de.dis2013.menu.MaklerSelectionMenu;
import de.dis2013.menu.Menu;
import de.dis2013.util.FormUtil;

/**
 * Klasse für die Menüs zur Verwaltung von Immobilien
 */
public class MaklerEditor {
	///Immobilienservice, der genutzt werden soll
	private ImmoService service;
	
	public MaklerEditor(ImmoService service) {
		this.service = service;
	}
	
	/**
	 * Zeigt die Maklerverwaltung
	 */
	public void showMaklerMenu() {
		//Menüoptionen
		final int NEW_MAKLER = 0;
		final int EDIT_MAKLER = 1;
		final int DELETE_MAKLER = 2;
		final int BACK = 3;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("EstateAgent-Verwaltung");
		maklerMenu.addEntry("Neuer EstateAgent", NEW_MAKLER);
		maklerMenu.addEntry("EstateAgent bearbeiten", EDIT_MAKLER);
		maklerMenu.addEntry("EstateAgent löschen", DELETE_MAKLER);
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_MAKLER:
					newMakler();
					break;
				case EDIT_MAKLER:
					editMakler();
					break;
				case DELETE_MAKLER:
					deleteMakler();
					break;
				case BACK:
					return;
			}
		}
	}
	
	/**
	 * Legt einen neuen EstateAgent an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public void newMakler() {
		EstateAgent m = new EstateAgent();
		
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		service.addMakler(m);
		
		System.out.println("EstateAgent mit der ID "+m.getId()+" wurde erzeugt.");
	}
	
	/**
	 * Berarbeitet einen EstateAgent, nachdem der Benutzer ihn ausgewählt hat
	 */
	public void editMakler() {
		//Menü zum selektieren des Maklers
		Menu maklerSelectionMenu = new MaklerSelectionMenu("EstateAgent editieren", service.getAllMakler());
		int id = maklerSelectionMenu.show();
		
		//Falls nicht "zurück" gewählt, EstateAgent bearbeiten
		if(id != MaklerSelectionMenu.BACK) {
			//EstateAgent laden
			EstateAgent m = service.getMaklerById(id);
			System.out.println("EstateAgent "+m.getName()+" wird bearbeitet. Leere Felder bleiben unverändert.");
			
			//Neue Daten abfragen
			String new_name = FormUtil.readString("Name ("+m.getName()+")");
			String new_address = FormUtil.readString("Adresse ("+m.getAddress()+")");
			String new_login = FormUtil.readString("Login ("+m.getLogin()+")");
			String new_password = FormUtil.readString("Passwort ("+m.getPassword()+")");
			
			//Neue Daten setzen
			if(!new_name.equals(""))
				m.setName(new_name);
			if(!new_address.equals(""))
				m.setAddress(new_address);
			if(!new_login.equals(""))
				m.setLogin(new_login);
			if(!new_password.equals(""))
				m.setPassword(new_password);
		}
	}
	
	/**
	 * Löscht einen EstateAgent, nachdem der Benutzer
	 * ihn ausgewählt hat.
	 */
	public void deleteMakler() {
		//Menü zum selektieren des Maklers
		Menu maklerSelectionMenu = new MaklerSelectionMenu("EstateAgent löschen", service.getAllMakler());
		int id = maklerSelectionMenu.show();
		
		//EstateAgent löschen falls nicht "zurück" ausgewählt wurde
		if(id != MaklerSelectionMenu.BACK) {
			EstateAgent m = service.getMaklerById(id);
			service.deleteMakler(m);
		}
	}
}
