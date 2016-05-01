package de.dis2013.editor;

import java.util.List;

import de.dis2013.core.DataAccessService;
import de.dis2013.data.Apartment;
import de.dis2013.data.House;
import de.dis2013.data.EstateAgent;
import de.dis2013.menu.AppartmentSelectionMenu;
import de.dis2013.menu.HouseSelectionMenu;
import de.dis2013.menu.Menu;
import de.dis2013.util.FormUtil;

public class EstateEditor {
	private DataAccessService service;
	
	private EstateAgent estateAgent;
	
	public EstateEditor(DataAccessService service, EstateAgent agent) {
		this.service = service;
		estateAgent = agent;
	}
	
	public void showImmoMenu() {
		final int NEW_HOUSE = 0;
		final int EDIT_HOUSE = 1;
		final int DELETE_HOUSE = 2;
		final int NEW_APPARTMENT = 3;
		final int EDIT_APPARTMENT = 4;
		final int DELETE_APPARTMENT = 5;
		final int BACK = 6;
		
		Menu estateMenu = new Menu("Estate Menu");
		estateMenu.addEntry("New House", NEW_HOUSE);
		estateMenu.addEntry("Edit House", EDIT_HOUSE);
		estateMenu.addEntry("Delete House", DELETE_HOUSE);
		
		estateMenu.addEntry("New Apartment", NEW_APPARTMENT);
		estateMenu.addEntry("Edit Apartment", EDIT_APPARTMENT);
		estateMenu.addEntry("Delete Apartment", DELETE_APPARTMENT);
		
		estateMenu.addEntry("Back", BACK);
		
		while(true) {
			int response = estateMenu.show();
			
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
	
	public void newHouse() {
		House house = new House();
		
		house.setCity(FormUtil.readString("Ort"));
		house.setPostalCode(FormUtil.readInt("PLZ"));
		house.setStreet(FormUtil.readString("Straße"));
		house.setStreetNumber(FormUtil.readString("Hausnummer"));
		house.setSquareArea(FormUtil.readInt("Fläche"));
		house.setFloors(FormUtil.readInt("Stockwerke"));
		house.setPrice(FormUtil.readInt("Kaufpreis"));
		house.setGarden(FormUtil.readBoolean("Garten"));
		house.setEstateAgent(this.estateAgent);
		
		service.persist(house);
	}
	
	public void editHouse() {
		List<House> houses = service.getAllHousesForAgent(estateAgent);
		
		HouseSelectionMenu hsm = new HouseSelectionMenu("List of houses", houses);
		int id = hsm.show();
		
		if(id != HouseSelectionMenu.BACK) {
			House house = (House)service.getById(House.class, id);
			
			System.out.println("House "+house.getStreet()+" "+house.getStreetNumber()+", "+house.getPostalCode()+" "+house.getCity()+" wird bearbeitet. Leere Felder bzw. Eingabe von 0 lässt Feld unverändert.");
			
			String newOrt = FormUtil.readString("Ort ("+house.getCity()+")");
			int newPlz = FormUtil.readInt("PLZ ("+house.getPostalCode()+")");
			String newStrasse = FormUtil.readString("Straße ("+house.getStreet()+")");
			String newHausNummer = FormUtil.readString("Hausnummer ("+house.getStreetNumber()+")");
			int newFlaeche = FormUtil.readInt("Fläche ("+house.getSquareArea()+")");
			int newStockwerke = FormUtil.readInt("Stockwerke ("+house.getFloors()+")");
			int newKaufpreis = FormUtil.readInt("Kaufpreis ("+house.getPrice()+")");
			boolean newGarten = FormUtil.readBoolean("Garten ("+(house.isGarden() ? "j" : "n")+")");
			
			if(!newOrt.equals(""))
				house.setCity(newOrt);
			
			if(!newStrasse.equals(""))
				house.setStreet(newStrasse);
			
			if(!newHausNummer.equals(""))
				house.setStreetNumber(newHausNummer);
			
			if(newPlz != 0)
				house.setPostalCode(newPlz);
			
			if(newFlaeche != 0)
				house.setSquareArea(newFlaeche);
			
			if(newStockwerke != 0)
				house.setFloors(newStockwerke);
			
			if(newKaufpreis != 0)
				house.setPrice(newKaufpreis);
			
			house.setGarden(newGarten);
            service.update(house);
		}
	}
	
	/**
	 * Zeigt die Liste von verwalteten Häusern und löscht das
	 * entsprechende House nach Auswahl
	 */
	public void deleteHouse() {
		List<House> houses = service.getAllHousesForAgent(estateAgent);
		
		HouseSelectionMenu hsm = new HouseSelectionMenu("List of Houses", houses);
		int id = hsm.show();
		
		if(id != HouseSelectionMenu.BACK) {
			House house = (House)service.getById(House.class, id);
			service.delete(house);
		}
	}
	
	public void newAppartment() {
		Apartment apartment = new Apartment();
		
		apartment.setCity(FormUtil.readString("Ort"));
		apartment.setPostalCode(FormUtil.readInt("PLZ"));
		apartment.setStreet(FormUtil.readString("Straße"));
		apartment.setStreetNumber(FormUtil.readString("Hausnummer"));
		apartment.setSquareArea(FormUtil.readInt("Fläche"));
		apartment.setFloor(FormUtil.readInt("Stockwerk"));
		apartment.setRent(FormUtil.readInt("Mietpreis"));
		apartment.setBuiltInKitchen(FormUtil.readBoolean("EBK"));
		apartment.setBalcony(FormUtil.readBoolean("Balkon"));
		apartment.setEstateAgent(this.estateAgent);
		
		service.persist(apartment);
	}
	
	public void editAppartment() {
		List<Apartment> apartments = service.getAllApartmentsForAgent(estateAgent);
		
		AppartmentSelectionMenu asm = new AppartmentSelectionMenu("List of Apartments", apartments);
		int id = asm.show();
		
		if(id != AppartmentSelectionMenu.BACK) {
			Apartment apartment = (Apartment)service.getById(Apartment.class, id);

			System.out.println("House "+apartment.getStreet()+" "+apartment.getStreetNumber()+", "+apartment.getPostalCode()+" "+apartment.getCity()+" wird bearbeitet. Leere Felder bzw. Eingabe von 0 lässt Feld unverändert.");
			
			String newOrt = FormUtil.readString("Ort ("+apartment.getCity()+")");
			int newPlz = FormUtil.readInt("PLZ ("+apartment.getPostalCode()+")");
			String newStrasse = FormUtil.readString("Straße ("+apartment.getStreet()+")");
			String newHausNummer = FormUtil.readString("Hausnummer ("+apartment.getStreetNumber()+")");
			int newFlaeche = FormUtil.readInt("Fläche ("+apartment.getSquareArea()+")");
			int newStockwerk = FormUtil.readInt("Stockwerk ("+apartment.getFloor()+")");
			int newMietpreis = FormUtil.readInt("Mietpreis ("+apartment.getRent()+")");
			boolean newEbk = FormUtil.readBoolean("EBK ("+(apartment.getBuiltInKitchen() ? "j" : "n")+")");
			boolean newBalkon = FormUtil.readBoolean("Balkon ("+(apartment.getBalcony() ? "j" : "n")+")");
			
			if(!newOrt.equals(""))
				apartment.setCity(newOrt);
			
			if(!newStrasse.equals(""))
				apartment.setStreet(newStrasse);
			
			if(!newHausNummer.equals(""))
				apartment.setStreetNumber(newHausNummer);
			
			if(newPlz != 0)
				apartment.setPostalCode(newPlz);
			
			if(newFlaeche != 0)
				apartment.setSquareArea(newFlaeche);
			
			if(newStockwerk != 0)
				apartment.setFloor(newStockwerk);
			
			if(newMietpreis != 0)
				apartment.setRent(newMietpreis);
			
			apartment.setBuiltInKitchen(newEbk);
			apartment.setBalcony(newBalkon);
            service.update(apartment);
		}
	}
	
	/**
	 * Zeigt die Liste von verwalteten Wohnungen und löscht die
	 * entsprechende Apartment nach Auswahl
	 */
	public void deleteAppartment() {
		List<Apartment> apartments = service.getAllApartmentsForAgent(estateAgent);
		
		AppartmentSelectionMenu asm = new AppartmentSelectionMenu("List of Apartments", apartments);
		int id = asm.show();
		
		if(id != HouseSelectionMenu.BACK) {
			Apartment apartment = (Apartment)service.getById(Apartment.class, id);
			service.delete(apartment);
		}
	}
}
