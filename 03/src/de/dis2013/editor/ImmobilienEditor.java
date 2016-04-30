package de.dis2013.editor;

import java.util.Set;

import de.dis2013.core.ImmoService;
import de.dis2013.data.Apartment;
import de.dis2013.data.House;
import de.dis2013.data.EstateAgent;
import de.dis2013.menu.AppartmentSelectionMenu;
import de.dis2013.menu.HouseSelectionMenu;
import de.dis2013.menu.Menu;
import de.dis2013.util.FormUtil;

/**
 * Klasse für die Menüs zur Verwaltung von Immobilien
 */
public class ImmobilienEditor {
	///Immobilienservice, der genutzt werden soll
	private ImmoService service;
	
	///Wird als Verwalter für die Immobilien eingetragen
	private EstateAgent verwalter;
	
	public ImmobilienEditor(ImmoService service, EstateAgent verwalter) {
		this.service = service;
		this.verwalter = verwalter;
	}
	
	/**
	 * Zeigt das Immobilien-Hauptmenü
	 */
	public void showImmoMenu() {
		//Menüoptionen
		final int NEW_HOUSE = 0;
		final int EDIT_HOUSE = 1;
		final int DELETE_HOUSE = 2;
		final int NEW_APPARTMENT = 3;
		final int EDIT_APPARTMENT = 4;
		final int DELETE_APPARTMENT = 5;
		final int BACK = 6;
		
		//Immobilienverwaltungsmenü
		Menu maklerMenu = new Menu("Immobilien-Verwaltung");
		maklerMenu.addEntry("Neues House anlegen", NEW_HOUSE);
		maklerMenu.addEntry("House bearbeiten", EDIT_HOUSE);
		maklerMenu.addEntry("House löschen", DELETE_HOUSE);
		
		maklerMenu.addEntry("Neue Apartment anlegen", NEW_APPARTMENT);
		maklerMenu.addEntry("Apartment bearbeiten", EDIT_APPARTMENT);
		maklerMenu.addEntry("Apartment löschen", DELETE_APPARTMENT);
		
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_HOUSE:
					newHouse();
					break;
				case EDIT_HOUSE:
					editHouse();
					break;
				case DELETE_HOUSE:
					deleteHouse();
					break;
				case NEW_APPARTMENT:
					newAppartment();
					break;
				case EDIT_APPARTMENT:
					editAppartment();
					break;
				case DELETE_APPARTMENT:
					deleteAppartment();
					break;
				case BACK:
					return;
			}
		}
	}
	
	/**
	 * Abfrage der Daten für ein neues House
	 */
	public void newHouse() {
		House h = new House();
		
		h.setCity(FormUtil.readString("Ort"));
		h.setPostalCode(FormUtil.readInt("PLZ"));
		h.setStreet(FormUtil.readString("Straße"));
		h.setStreetNumber(FormUtil.readString("Hausnummer"));
		h.setSquareArea(FormUtil.readInt("Fläche"));
		h.setFloors(FormUtil.readInt("Stockwerke"));
		h.setPrice(FormUtil.readInt("Kaufpreis"));
		h.setGarden(FormUtil.readBoolean("Garten"));
		h.setEstateAgent(this.verwalter);
		
		service.addHaus(h);
	}
	
	/**
	 * Lässt den Benutzer ein House zum bearbeiten auswählen
	 * und fragt anschließend die neuen Daten ab.
	 */
	public void editHouse() {
		//Alle Häuser suchen, die vom EstateAgent verwaltet werden
		Set<House> haeuser = service.getAllHaeuserForMakler(verwalter);
		
		//Auswahlmenü für das zu bearbeitende House
		HouseSelectionMenu hsm = new HouseSelectionMenu("Liste der verwalteten Häuser", haeuser);
		int id = hsm.show();
		
		//Falls nicht der Eintrag "zurück" gewählt wurde, House bearbeiten
		if(id != HouseSelectionMenu.BACK) {
			//Gewähltes House laden
			House h = service.getHausById(id);
			
			System.out.println("House "+h.getStreet()+" "+h.getStreetNumber()+", "+h.getPostalCode()+" "+h.getCity()+" wird bearbeitet. Leere Felder bzw. Eingabe von 0 lässt Feld unverändert.");
			
			//Neue Daten abfragen
			String newOrt = FormUtil.readString("Ort ("+h.getCity()+")");
			int newPlz = FormUtil.readInt("PLZ ("+h.getPostalCode()+")");
			String newStrasse = FormUtil.readString("Straße ("+h.getStreet()+")");
			String newHausNummer = FormUtil.readString("Hausnummer ("+h.getStreetNumber()+")");
			int newFlaeche = FormUtil.readInt("Fläche ("+h.getSquareArea()+")");
			int newStockwerke = FormUtil.readInt("Stockwerke ("+h.getFloors()+")");
			int newKaufpreis = FormUtil.readInt("Kaufpreis ("+h.getPrice()+")");
			boolean newGarten = FormUtil.readBoolean("Garten ("+(h.isGarden() ? "j" : "n")+")");
			
			//Neue Daten setzen
			if(!newOrt.equals(""))
				h.setCity(newOrt);
			
			if(!newStrasse.equals(""))
				h.setStreet(newStrasse);
			
			if(!newHausNummer.equals(""))
				h.setStreetNumber(newHausNummer);
			
			if(newPlz != 0)
				h.setPostalCode(newPlz);
			
			if(newFlaeche != 0)
				h.setSquareArea(newFlaeche);
			
			if(newStockwerke != 0)
				h.setFloors(newStockwerke);
			
			if(newKaufpreis != 0)
				h.setPrice(newKaufpreis);
			
			h.setGarden(newGarten);
		}
	}
	
	/**
	 * Zeigt die Liste von verwalteten Häusern und löscht das
	 * entsprechende House nach Auswahl
	 */
	public void deleteHouse() {
		//Alle Häuser suchen, die vom EstateAgent verwaltet werden
		Set<House> haeuser = service.getAllHaeuserForMakler(verwalter);
		
		//Auswahlmenü für das zu bearbeitende House
		HouseSelectionMenu hsm = new HouseSelectionMenu("Liste der verwalteten Häuser", haeuser);
		int id = hsm.show();
		
		//Falls nicht der Eintrag "zurück" gewählt wurde, House löschen
		if(id != HouseSelectionMenu.BACK) {
			House h = service.getHausById(id);
			service.deleteHouse(h);
		}
	}
	
	/**
	 * Abfrage der Daten für eine neue Apartment
	 */
	public void newAppartment() {
		Apartment w = new Apartment();
		
		w.setCity(FormUtil.readString("Ort"));
		w.setPostalCode(FormUtil.readInt("PLZ"));
		w.setStreet(FormUtil.readString("Straße"));
		w.setStreetNumber(FormUtil.readString("Hausnummer"));
		w.setSquareArea(FormUtil.readInt("Fläche"));
		w.setFloor(FormUtil.readInt("Stockwerk"));
		w.setRent(FormUtil.readInt("Mietpreis"));
		w.setBuiltInKitchen(FormUtil.readBoolean("EBK"));
		w.setBalcony(FormUtil.readBoolean("Balkon"));
		w.setEstateAgent(this.verwalter);
		
		service.addWohnung(w);
	}
	
	/**
	 * Lässt den Benutzer eine Apartment zum bearbeiten auswählen
	 * und fragt anschließend die neuen Daten ab.
	 */
	public void editAppartment() {
		//Alle Wohnungen suchen, die vom EstateAgent verwaltet werden
		Set<Apartment> wohnungen = service.getAllWohnungenForMakler(verwalter);
		
		//Auswahlmenü für die zu bearbeitende Apartment
		AppartmentSelectionMenu asm = new AppartmentSelectionMenu("Liste der verwalteten Wohnungen", wohnungen);
		int id = asm.show();
		
		//Falls nicht der Eintrag "zurück" gewählt wurde, Apartment bearbeiten
		if(id != AppartmentSelectionMenu.BACK) {
			//Apartment laden
			Apartment w = service.getWohnungById(id);
			
			System.out.println("House "+w.getStreet()+" "+w.getStreetNumber()+", "+w.getPostalCode()+" "+w.getCity()+" wird bearbeitet. Leere Felder bzw. Eingabe von 0 lässt Feld unverändert.");
			
			//Neue Daten abfragen
			String newOrt = FormUtil.readString("Ort ("+w.getCity()+")");
			int newPlz = FormUtil.readInt("PLZ ("+w.getPostalCode()+")");
			String newStrasse = FormUtil.readString("Straße ("+w.getStreet()+")");
			String newHausNummer = FormUtil.readString("Hausnummer ("+w.getStreetNumber()+")");
			int newFlaeche = FormUtil.readInt("Fläche ("+w.getSquareArea()+")");
			int newStockwerk = FormUtil.readInt("Stockwerk ("+w.getFloor()+")");
			int newMietpreis = FormUtil.readInt("Mietpreis ("+w.getRent()+")");
			boolean newEbk = FormUtil.readBoolean("EBK ("+(w.isBuiltInKitchen() ? "j" : "n")+")");
			boolean newBalkon = FormUtil.readBoolean("Balkon ("+(w.isBalcony() ? "j" : "n")+")");
			
			//Neue Daten setzen
			if(!newOrt.equals(""))
				w.setCity(newOrt);
			
			if(!newStrasse.equals(""))
				w.setStreet(newStrasse);
			
			if(!newHausNummer.equals(""))
				w.setStreetNumber(newHausNummer);
			
			if(newPlz != 0)
				w.setPostalCode(newPlz);
			
			if(newFlaeche != 0)
				w.setSquareArea(newFlaeche);
			
			if(newStockwerk != 0)
				w.setFloor(newStockwerk);
			
			if(newMietpreis != 0)
				w.setRent(newMietpreis);
			
			w.setBuiltInKitchen(newEbk);
			w.setBalcony(newBalkon);
		}
	}
	
	/**
	 * Zeigt die Liste von verwalteten Wohnungen und löscht die
	 * entsprechende Apartment nach Auswahl
	 */
	public void deleteAppartment() {
		//Alle Wohnungen suchen, die vom EstateAgent verwaltet werden
		Set<Apartment> wohnungen = service.getAllWohnungenForMakler(verwalter);
		
		//Auswahlmenü für die zu bearbeitende Apartment
		AppartmentSelectionMenu asm = new AppartmentSelectionMenu("Liste der verwalteten Wohnungen", wohnungen);
		int id = asm.show();
		
		//Falls nicht der Eintrag "zurück" gewählt wurde, Apartment löschen
		if(id != HouseSelectionMenu.BACK) {
			Apartment w = service.getWohnungById(id);
			service.deleteWohnung(w);
		}
	}
}
