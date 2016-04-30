package de.dis2013.editor;

import java.util.Iterator;
import java.util.Set;

import de.dis2013.core.ImmoService;
import de.dis2013.data.*;
import de.dis2013.data.House;
import de.dis2013.menu.AppartmentSelectionMenu;
import de.dis2013.menu.HouseSelectionMenu;
import de.dis2013.menu.Menu;
import de.dis2013.menu.PersonSelectionMenu;
import de.dis2013.util.FormUtil;
import de.dis2013.util.Helper;

/**
 * Klasse für die Menüs zur Verwaltung von Verträgen
 */
public class VertragsEditor {
	///Immobilien-Service, der genutzt werden soll
	private ImmoService service;
	
	///EstateAgent, zu dessen Immobilien Verträge geschlossen werden dürfen
	private EstateAgent verwalter;
	
	public VertragsEditor(ImmoService service, EstateAgent verwalter) {
		this.service = service;
		this.verwalter = verwalter;
	}
	
	/**
	 * Vertragsmenü
	 */
	public void showVertragsMenu() {
		//Menüoptionen
		final int NEW_LEASING_CONTRACT = 0;
		final int NEW_SALE_CONTRACT = 1;
		final int SHOW_CONTRACTS = 2;
		final int BACK = 3;
		
		//Vertragsverwaltung
		Menu maklerMenu = new Menu("Vertrags-Verwaltung");
		maklerMenu.addEntry("Neuer TenancyContract", NEW_LEASING_CONTRACT);
		maklerMenu.addEntry("Neuer PurchaseContract", NEW_SALE_CONTRACT);
		maklerMenu.addEntry("Verträge ansehen", SHOW_CONTRACTS);
		
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_LEASING_CONTRACT:
					newMietvertrag();
					break;
				case NEW_SALE_CONTRACT:
					newKaufvertrag();
					break;
				case SHOW_CONTRACTS:
					zeigeVertraege();
					break;
				case BACK:
					return;
			}
		}
	}
	
	public void zeigeVertraege() {
		//Mietverträge anzeigen
		System.out.println("Mietverträge\n-----------------");
		Set<TenancyContract> mvs = service.getAllMietvertraegeForMakler(verwalter);
		Iterator<TenancyContract> itmv = mvs.iterator();
		while(itmv.hasNext()) {
			TenancyContract mv = itmv.next();
			System.out.println("TenancyContract "+mv.getContractNumber()+"\n"+
							"\tGeschlossen am "+Helper.dateToString(mv.getDate())+" in "+mv.getPlace()+"\n"+
							"\tMieter:        "+mv.getVertragspartner().getFirstName()+" "+mv.getVertragspartner().getName()+"\n"+
							"\tApartment:       "+mv.getApartment().getStreet()+" "+mv.getApartment().getStreetNumber()+", "+mv.getApartment().getPostalCode()+" "+mv.getApartment().getCity()+"\n"+
							"\tMietbeginn:    "+Helper.dateToString(mv.getStartDate())+", Dauer: "+mv.getDuration()+" Monate\n"+
							"\tMietpreis:     "+mv.getApartment().getRent()+" Euro, Nebenkosten: "+mv.getAdditionalCosts()+" Euro\n");
		}
		
		System.out.println("");
		
		//Kaufverträge anzeigen
		System.out.println("Kaufverträge\n-----------------");
		Set<PurchaseContract> kvs = service.getAllKaufvertraegeForMakler(verwalter);
		Iterator<PurchaseContract> itkv = kvs.iterator();
		while(itkv.hasNext()) {
			PurchaseContract kv = itkv.next();
			System.out.println("PurchaseContract "+kv.getContractNumber()+"\n"+
							"\tGeschlossen am "+Helper.dateToString(kv.getDate())+" in "+kv.getPlace()+"\n"+
							"\tMieter:        "+kv.getVertragspartner().getFirstName()+" "+kv.getVertragspartner().getName()+"\n"+
							"\tHouse:          "+kv.getHouse().getStreet()+" "+kv.getHouse().getStreetNumber()+", "+kv.getHouse().getPostalCode()+" "+kv.getHouse().getCity()+"\n"+
							"\tKaufpreis:     "+kv.getHouse().getPrice()+" Euro\n"+
							"\tRaten:         "+kv.getNumberOfInstallments()+", Ratenzins: "+kv.getInterestRate()+"%\n");
		}
	}
	
	
	/**
	 * Menü zum anlegen eines neuen Mietvertrags
	 */
	public void newMietvertrag() {
		//Alle Wohnungen des Maklers finden
		Set<Apartment> wohnungen = service.getAllWohnungenForMakler(verwalter);
		
		//Auswahlmenü für die Wohnungen 
		AppartmentSelectionMenu asm = new AppartmentSelectionMenu("Apartment für Contract auswählen", wohnungen);
		int wid = asm.show();
		
		//Falls kein Abbruch: Auswahl der Person
		if(wid != AppartmentSelectionMenu.BACK) {
			//Alle Personen laden
			Set<Person> personen = service.getAllPersons();
			
			//Menü zur Auswahl der Person
			PersonSelectionMenu psm = new PersonSelectionMenu("Person für Contract auswählen", personen);
			int pid = psm.show();
			
			//Falls kein Abbruch: Vertragsdaten abfragen und Contract anlegen
			if(pid != PersonSelectionMenu.BACK) {
				TenancyContract m = new TenancyContract();
		
				m.setApartment(service.getWohnungById(wid));
				m.setVertragspartner(service.getPersonById(pid));
				m.setContractNumber(FormUtil.readInt("Vertragsnummer"));
				m.setDate(FormUtil.readDate("Datum"));
				m.setPlace(FormUtil.readString("Ort"));
				m.setStartDate(FormUtil.readDate("Mietbeginn"));
				m.setDuration(FormUtil.readInt("Dauer in Monaten"));
				m.setAdditionalCosts(FormUtil.readInt("Nebenkosten"));
				
				service.addMietvertrag(m);
				
				System.out.println("TenancyContract mit der ID "+m.getId()+" wurde erzeugt.");
			}
		}
	}
	
	/**
	 * Menü zum anlegen eines neuen Kaufvertrags
	 */
	public void newKaufvertrag() {
		//Alle Häuser des Maklers finden
		Set<House> haeuser = service.getAllHaeuserForMakler(verwalter);
		
		//Auswahlmenü für das House
		HouseSelectionMenu asm = new HouseSelectionMenu("House für Contract auswählen", haeuser);
		int hid = asm.show();
		
		//Falls kein Abbruch: Auswahl der Person
		if(hid != AppartmentSelectionMenu.BACK) {
			//Alle Personen laden
			Set<Person> personen = service.getAllPersons();
			
			//Menü zur Auswahl der Person
			PersonSelectionMenu psm = new PersonSelectionMenu("Person für Contract auswählen", personen);
			int pid = psm.show();
			
			//Falls kein Abbruch: Vertragsdaten abfragen und Contract anlegen
			if(pid != PersonSelectionMenu.BACK) {
				PurchaseContract k = new PurchaseContract();
		
				k.setHouse(service.getHausById(hid));
				k.setVertragspartner(service.getPersonById(pid));
				k.setContractNumber(FormUtil.readInt("Vertragsnummer"));
				k.setDate(FormUtil.readDate("Datum"));
				k.setPlace(FormUtil.readString("Ort"));
				k.setNumberOfInstallments(FormUtil.readInt("Anzahl Raten"));
				k.setInterestRate(FormUtil.readInt("Ratenzins"));
				
				service.addKaufvertrag(k);
				
				System.out.println("PurchaseContract mit der ID "+k.getId()+" wurde erzeugt.");
			}
		}
	}
}
