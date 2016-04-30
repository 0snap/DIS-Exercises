package de.dis2013;

import de.dis2013.authentication.MaklerAuthenticator;
import de.dis2013.authentication.PropertiesFileAuthenticator;
import de.dis2013.core.ImmoService;
import de.dis2013.editor.ImmobilienEditor;
import de.dis2013.editor.MaklerEditor;
import de.dis2013.editor.PersonEditor;
import de.dis2013.editor.VertragsEditor;
import de.dis2013.menu.Menu;

/**
 * Hauptklasse, die das Hauptmenü zeigt
 */
public class Main {
	private static ImmoService service;
	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		service = new ImmoService();
		showMainMenu();
	}
	
	/**
	 * Zeigt das Hauptmenü
	 */
	public static void showMainMenu() {
		//Menüoptionen
		final int MENU_MAKLER = 0;
		final int MENU_PERSON= 1;
		final int MENU_IMMO = 2;
		final int MENU_VERTRAG = 3;
		final int QUIT = 4;
		
		//Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("EstateAgent-Verwaltung", MENU_MAKLER);
		mainMenu.addEntry("Personen-Verwaltung", MENU_PERSON);
		mainMenu.addEntry("Immobilien-Verwaltung", MENU_IMMO);
		mainMenu.addEntry("Vertragsmenü", MENU_VERTRAG);
		mainMenu.addEntry("Beenden", QUIT);
		
		//Authentifizierungsmöglichkeiten
		PropertiesFileAuthenticator pfa = new PropertiesFileAuthenticator("admin.properties");
		MaklerAuthenticator ma = new MaklerAuthenticator(service);
		
		//Testdaten
		service.addTestData();
		
		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_MAKLER:
					if(pfa.authenticate()) {
						MaklerEditor me = new MaklerEditor(service);
						me.showMaklerMenu();
					}
					break;
				case MENU_PERSON:
					if(ma.authenticate()) {
						PersonEditor pe = new PersonEditor(service);
						pe.showPersonMenu();
					}
					break;
				case MENU_IMMO:
					if(ma.authenticate()) {
						ImmobilienEditor ie = new ImmobilienEditor(service, ma.getLastAuthenticatedMakler());
						ie.showImmoMenu();
					}
					break;
				case MENU_VERTRAG:
					if(ma.authenticate()) {
						VertragsEditor ve = new VertragsEditor(service, ma.getLastAuthenticatedMakler());
						ve.showVertragsMenu();
					}
					break;
				case QUIT:
					return;
			}
		}
	}
}
