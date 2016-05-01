package de.dis2013.editor;

import java.util.Iterator;
import java.util.Set;

import de.dis2013.core.EstateService;
import de.dis2013.data.*;
import de.dis2013.data.House;
import de.dis2013.menu.AppartmentSelectionMenu;
import de.dis2013.menu.HouseSelectionMenu;
import de.dis2013.menu.Menu;
import de.dis2013.menu.PersonSelectionMenu;
import de.dis2013.util.FormUtil;
import de.dis2013.util.Helper;

public class ContractEditor {
	private EstateService service;
	
	private EstateAgent estateAgent;
	
	public ContractEditor(EstateService service, EstateAgent verwalter) {
		this.service = service;
		this.estateAgent = verwalter;
	}
	
	public void showContractMenu() {
		final int NEW_LEASING_CONTRACT = 0;
		final int NEW_SALE_CONTRACT = 1;
		final int SHOW_CONTRACTS = 2;
		final int BACK = 3;
		
		Menu maklerMenu = new Menu("Contract-Menu");
		maklerMenu.addEntry("New TenancyContract", NEW_LEASING_CONTRACT);
		maklerMenu.addEntry("New PurchaseContract", NEW_SALE_CONTRACT);
		maklerMenu.addEntry("Show Contracts", SHOW_CONTRACTS);
		
		maklerMenu.addEntry("Back to main menu", BACK);
		
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
		System.out.println("TenancyContracts\n-----------------");
		Set<TenancyContract> mvs = service.getAllMietvertraegeForMakler(estateAgent);
		Iterator<TenancyContract> itmv = mvs.iterator();
		while(itmv.hasNext()) {
			TenancyContract tenancyContract = itmv.next();
			System.out.println("TenancyContract "+tenancyContract.getContractNumber()+"\n"+
							"\tGeschlossen am "+Helper.dateToString(tenancyContract.getDate())+" in "+tenancyContract.getPlace()+"\n"+
							"\tMieter:        "+tenancyContract.getPerson().getFirstName()+" "+tenancyContract.getPerson().getName()+"\n"+
							"\tApartment:       "+tenancyContract.getApartment().getStreet()+" "+tenancyContract.getApartment().getStreetNumber()+", "+tenancyContract.getApartment().getPostalCode()+" "+tenancyContract.getApartment().getCity()+"\n"+
							"\tMietbeginn:    "+Helper.dateToString(tenancyContract.getStartDate())+", Dauer: "+tenancyContract.getDuration()+" Monate\n"+
							"\tMietpreis:     "+tenancyContract.getApartment().getRent()+" Euro, Nebenkosten: "+tenancyContract.getAdditionalCosts()+" Euro\n");
		}
		
		System.out.println("");
		
		System.out.println("PurchaseContracts\n-----------------");
		Set<PurchaseContract> kvs = service.getAllKaufvertraegeForMakler(estateAgent);
		Iterator<PurchaseContract> itkv = kvs.iterator();
		while(itkv.hasNext()) {
			PurchaseContract kv = itkv.next();
			System.out.println("PurchaseContract "+kv.getContractNumber()+"\n"+
							"\tGeschlossen am "+Helper.dateToString(kv.getDate())+" in "+kv.getPlace()+"\n"+
							"\tMieter:        "+kv.getPerson().getFirstName()+" "+kv.getPerson().getName()+"\n"+
							"\tHouse:          "+kv.getHouse().getStreet()+" "+kv.getHouse().getStreetNumber()+", "+kv.getHouse().getPostalCode()+" "+kv.getHouse().getCity()+"\n"+
							"\tKaufpreis:     "+kv.getHouse().getPrice()+" Euro\n"+
							"\tRaten:         "+kv.getNumberOfInstallments()+", Ratenzins: "+kv.getInterestRate()+"%\n");
		}
	}
	
	

	public void newMietvertrag() {
		Set<Apartment> wohnungen = service.getAllWohnungenForMakler(estateAgent);
		
		AppartmentSelectionMenu asm = new AppartmentSelectionMenu("Apartment für Contract auswählen", wohnungen);
		int wid = asm.show();
		
		if(wid != AppartmentSelectionMenu.BACK) {
			Set<Person> personen = service.getAllPersons();
			
			PersonSelectionMenu psm = new PersonSelectionMenu("Person für Contract auswählen", personen);
			int pid = psm.show();
			
			if(pid != PersonSelectionMenu.BACK) {
				TenancyContract m = new TenancyContract();
		
				m.setApartment(service.getWohnungById(wid));
				m.setPerson(service.getPersonById(pid));
				m.setContractNumber(FormUtil.readInt("Contractnummer"));
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
	

	public void newKaufvertrag() {
		Set<House> haeuser = service.getAllHaeuserForMakler(estateAgent);
		
		HouseSelectionMenu asm = new HouseSelectionMenu("House für Contract auswählen", haeuser);
		int houseId = asm.show();
		
		if(houseId != AppartmentSelectionMenu.BACK) {
			Set<Person> personen = service.getAllPersons();
			
			PersonSelectionMenu psm = new PersonSelectionMenu("Person für Contract auswählen", personen);
			int pid = psm.show();
			
			if(pid != PersonSelectionMenu.BACK) {
				PurchaseContract k = new PurchaseContract();
		
				k.setHouse(service.getHausById(houseId));
				k.setPerson(service.getPersonById(pid));
				k.setContractNumber(FormUtil.readInt("Contractnummer"));
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
