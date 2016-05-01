package de.dis2013.editor;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.dis2013.core.DataAccessService;
import de.dis2013.data.*;
import de.dis2013.data.House;
import de.dis2013.menu.AppartmentSelectionMenu;
import de.dis2013.menu.HouseSelectionMenu;
import de.dis2013.menu.Menu;
import de.dis2013.menu.PersonSelectionMenu;
import de.dis2013.util.FormUtil;
import de.dis2013.util.Helper;

public class ContractEditor {
	private DataAccessService service;
	
	private EstateAgent estateAgent;
	
	public ContractEditor(DataAccessService service, EstateAgent verwalter) {
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
		List<TenancyContract> mvs = service.getAllTenancyContractsForAgent(estateAgent);
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
		List<PurchaseContract> kvs = service.getAllPurchaseContractsForAgent(estateAgent);
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
		List<Apartment> apartments = service.getAllApartmentsForAgent(estateAgent);
		
		AppartmentSelectionMenu appartmentMenu = new AppartmentSelectionMenu("Select Apartment for Contract", apartments);
		int apartmentId = appartmentMenu.show();

		if(apartmentId != AppartmentSelectionMenu.BACK) {
			List<Person> persons = service.getAllPersons();
			
			PersonSelectionMenu psm = new PersonSelectionMenu("Select Person for Contract", persons);
			int personId = psm.show();
			
			if(personId != PersonSelectionMenu.BACK) {
				TenancyContract tenancyContract = new TenancyContract();
		
				tenancyContract.setApartment((Apartment)service.getById(Apartment.class, apartmentId));
				tenancyContract.setPerson((Person)service.getById(Person.class, personId));
				tenancyContract.setContractNumber(FormUtil.readInt("Contractnummer"));
				tenancyContract.setDate(FormUtil.readDate("Datum"));
				tenancyContract.setPlace(FormUtil.readString("Ort"));
				tenancyContract.setStartDate(FormUtil.readDate("Mietbeginn"));
				tenancyContract.setDuration(FormUtil.readInt("Dauer in Monaten"));
				tenancyContract.setAdditionalCosts(FormUtil.readInt("Nebenkosten"));
				
				service.persist(tenancyContract);
				
				System.out.println("TenancyContract created with ID "+tenancyContract.getId()+".");
			}
		}
	}
	

	public void newKaufvertrag() {
		List<House> houses = service.getAllHousesForAgent(estateAgent);
		
		HouseSelectionMenu houseMenu = new HouseSelectionMenu("Select House for Contract", houses);
		int houseId = houseMenu.show();
		
		if(houseId != AppartmentSelectionMenu.BACK) {
			List<Person> persons = service.getAllPersons();
			
			PersonSelectionMenu personMenu = new PersonSelectionMenu("Select Person for Contract", persons);
			int personId = personMenu.show();
			
			if(personId != PersonSelectionMenu.BACK) {
				PurchaseContract purchaseContract = new PurchaseContract();
		
				purchaseContract.setHouse((House)service.getById(House.class, houseId));
				purchaseContract.setPerson((Person)service.getById(Person.class, personId));
				purchaseContract.setContractNumber(FormUtil.readInt("Contractnummer"));
				purchaseContract.setDate(FormUtil.readDate("Datum"));
				purchaseContract.setPlace(FormUtil.readString("Ort"));
				purchaseContract.setNumberOfInstallments(FormUtil.readInt("Anzahl Raten"));
				purchaseContract.setInterestRate(FormUtil.readInt("Ratenzins"));
				
				service.persist(purchaseContract);
				
				System.out.println("PurchaseContract created with ID "+purchaseContract.getId()+".");
			}
		}
	}
}
