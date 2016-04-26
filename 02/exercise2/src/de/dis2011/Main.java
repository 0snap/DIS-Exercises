package de.dis2011;


import de.dis2011.data.*;

import java.sql.Date;
import java.util.List;


public class Main {

	// never changing constants back and quit
	private static final int BACK = 0;
	private static final int QUIT = 0;

	private static final int MENU_ESTATE_AGENTS = 1;
	private static final int MENU_ESTATES = 2;
    private static final int MENU_CONTRACT = 3;
	private static final int MENU_PERSON = 4;

	// commands inside the menus:
	private static final int NEW = 1;
	private static final int EDIT = 2;
	private static final int DELETE = 3;
    private static final int LIST = 4;

	private DataAccessHelper accessHelper;


	public static void main(String[] args) {
		Main main = new Main();
		main.showMainMenu();
	}

	// non static application logic starts here:

	Main() {
		accessHelper = new DataAccessHelper();
	}

	private void showMainMenu() {

		Menu mainMenu = new Menu("Main menu");
        mainMenu.addEntry("EstateAgent Administration", MENU_ESTATE_AGENTS);
        mainMenu.addEntry("Estate Administration", MENU_ESTATES);
		mainMenu.addEntry("Contract Management", MENU_CONTRACT);
		mainMenu.addEntry("Person Management", MENU_PERSON);
		mainMenu.addEntry("Quit!", QUIT);
		
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_ESTATE_AGENTS:
					showEstateAgentMenu();
					break;
				case MENU_ESTATES:
					showEstateMenu();
					break;
                case MENU_CONTRACT:
                    showContractMenu();
                    break;
				case MENU_PERSON:
					showPersonMenu();
					break;
				case QUIT:
					return;
			}
		}
	}
	
	private void showEstateAgentMenu() {

		Menu agentMenu = new Menu("EstateAgent Administration");
		agentMenu.addEntry("New EstateAgent", NEW);
		agentMenu.addEntry("Edit EstateAgent", EDIT);
		agentMenu.addEntry("Back to main menu", BACK);
		
		while(true) {
			int response = agentMenu.show();
			
			switch(response) {
				case NEW:
					newEstateAgent();
					break;
				case EDIT:
					editEstateAgent();
					break;
				case BACK:
					return;
			}
		}
	}

	private void showEstateMenu() {

		Menu estateMenu = new Menu("Estate Administration");
		estateMenu.addEntry("New Estate", NEW);
		estateMenu.addEntry("Edit Estate", EDIT);
		estateMenu.addEntry("Delete Estate", DELETE);
		estateMenu.addEntry("Back to main menu", BACK);

		while(true) {
			int response = estateMenu.show();

			switch(response) {
				case NEW:
					newEstate();
					break;
				case EDIT:
					editEstate();
					break;
				case DELETE:
					deleteEstate();
					break;
				case BACK:
					return;
			}
		}
	}

	private void showContractMenu() {
        Menu contractMenu = new Menu("Contract management");
        contractMenu.addEntry("List all Contracts", LIST);
        contractMenu.addEntry("Sign Contract", NEW);
        contractMenu.addEntry("Back to main menu", BACK);

        while(true) {
            int response = contractMenu.show();

            switch(response) {
                case NEW:
                    newContract();
                    break;
                case LIST:
                    showContractList();
                    break;
                case BACK:
                    return;
            }
        }

    }

	private void showPersonMenu() {

		Menu personMenu = new Menu("Person Administration");
		personMenu.addEntry("New Person", NEW);
		personMenu.addEntry("Back to main menu", BACK);

		while(true) {
			int response = personMenu.show();

			switch(response) {
				case NEW:
					newPerson();
					break;
				case BACK:
					return;
			}
		}
	}


    private void showContractList(){
        Menu contractList = new Menu("Contract list");
        contractList.addEntry("Back to contract management", BACK);

        List<Contract> contracts = accessHelper.getContractList();
        for (Contract contract: contracts){
            //change i+1 to id of Contract
            contractList.addLabel(contract.toString());
        }
		contractList.show();
    }

	public void newContract() {
        Contract contract = readContractProperties();
		int id = accessHelper.save(contract);
		System.out.println("Contract with ID " + id + " saved successfully.");
    }

	public void newEstateAgent() {
		EstateAgent agent = new EstateAgent();
		readEstateAgentProperties(agent);
		int id = accessHelper.save(agent);
		System.out.println("EstateAgent with ID " + id + " saved successfully.");
	}

	private void editEstateAgent() {
		EstateAgent agent = loginEstateAgent();

		if(agent == null) {
			//login failed
			return;
		}
		// else
		readEstateAgentProperties(agent);
		accessHelper.save(agent);
	}

	private EstateAgent loginEstateAgent() {
		String id = FormUtil.readString("Agent Login");
		EstateAgent agent = accessHelper.loadEstateAgent(id);
		if(agent == null){
			System.out.println("EstateAgent is not known, please ensure you provided the right Login!");
            return null;
		}
		if (!FormUtil.readString("Agent password").equals(agent.getPassword())) {
			System.out.println("Incorrect password! You are not allowed to proceed");
			return null;
		}
		return agent;
	}

	/** small helper for reading stuff from cli and passing it to an agent*/
	private void readEstateAgentProperties(EstateAgent agent) {
		agent.setName(FormUtil.readString("Name"));
		agent.setAddress(FormUtil.readString("Adresse"));
		agent.setLogin(FormUtil.readString("Login"));
		agent.setPassword(FormUtil.readString("Passwort"));
	}

	public void newPerson() {
		Person person = new Person();
		person.setFirstName(FormUtil.readString("First name"));
		person.setName(FormUtil.readString("Name"));
		person.setAddress(FormUtil.readString("Adresse"));
		int id = accessHelper.save(person);
		System.out.println("Person with ID " + id + " saved successfully.");
	}

	public void newEstate() {
		EstateAgent agent = loginEstateAgent();
		if(agent == null) {
			return;
		}
		// looged in, create new estate of this agent
		Estate estate = new Estate();
		estate.setEstateAgent(agent.getId());
		int type = readEstateType();
		int savedObjectId = readAndPersistEstate(estate, type);

		System.out.println("Estate with ID " + savedObjectId + " saved successfully.");
	}


	public void editEstate() {
		EstateAgent agent = loginEstateAgent();
		if(agent == null) {
			return;
		}
		// looged in, query estate of this agent
		int type = readEstateType();
		int id = Integer.parseInt(FormUtil.readString("Estate ID to edit"));
		Estate estate = accessHelper.loadEstate(id, type);
		if(estate.getEstateAgent() != agent.getId()) {
			// logged in agent does not own this estate, cannot edit!
			System.out.println("You do not own this estate, you cannot edit it.");
			return;
		}
		readAndPersistEstate(estate, type);
	}

    public void deleteEstate() {
		EstateAgent agent = loginEstateAgent();
		if(agent == null) {
			return;
		}
		// looged in, query estate of this agent
		int type = readEstateType();
        int id = Integer.parseInt(FormUtil.readString("Estate ID to delete"));

		Estate estate = accessHelper.loadEstate(id, type);
		if(estate.getEstateAgent() != agent.getId()) {
			// logged in agent does not own this estate, cannot delete!
			System.out.println("You do not own this estate, you cannot delete it.");
			return;
		}
        accessHelper.deleteEstate(id, type);

        System.out.println("Estate with ID " + id + " deleted successfully.");

    }

	/** small helper for reading stuff from cli and passing it to an agent*/
	private void readEstateProperties(Estate estate) {
		estate.setCity(FormUtil.readString("City"));
		estate.setPostalCode(FormUtil.readString("ZIP"));
		estate.setStreet(FormUtil.readString("Street"));
		estate.setStreetNumber(FormUtil.readInt("Street number"));
		estate.setSquareArea(FormUtil.readString("Square area"));
	}

	private House readHouseProperties(Estate estate) {
		House house = new House(estate);
		house.setFloors(FormUtil.readInt("Floors"));
		house.setPrice(FormUtil.readInt("Price"));
		house.setGarden(FormUtil.readInt("Garden (0=no, 1=yes)"));
		house.setPerson(FormUtil.readInt("Person (Buyer) ID"));
		house.setPurchaseContract(FormUtil.readInt("Purchase Contract ID"));
		return house;
	}

	private Apartment readApartmentProperties(Estate estate) {
		Apartment apartment = new Apartment(estate);
		apartment.setFloor(FormUtil.readInt("Floor"));
		apartment.setRent(FormUtil.readInt("Rent"));
		apartment.setBalcony(FormUtil.readInt("Balcony (0=no, 1=yes)"));
		apartment.setBuiltInKitchen(FormUtil.readInt("Built in kitchen (0=no, 1=yes)"));
		apartment.setPerson(FormUtil.readInt("Person (Buyer) ID"));
		apartment.setTenancyContract(FormUtil.readInt("Tenancy Contract ID"));
		return apartment;
	}

	private Contract readContractProperties() {
		Contract contract = new Contract();
		contract.setDate(new Date(new java.util.Date().getTime()));
		contract.setPlace(FormUtil.readString("Place"));
		boolean isPurchase = FormUtil.readInt("Contract type (0=purchase, 1=tenancy)") == 0;
		if(isPurchase) {
			PurchaseContract purchaseContract = new PurchaseContract(contract);
			purchaseContract.setInterest(FormUtil.readInt("Number of interested people"));
			purchaseContract.setInstallments(FormUtil.readInt("Number of installments"));
			purchaseContract.setSeller(FormUtil.readInt("Person ID - Seller"));
			purchaseContract.setBuyer(FormUtil.readInt("Person ID - Buyer"));
			return purchaseContract;
		}
		else {
			TenancyContract tenancyContract = new TenancyContract(contract);
			tenancyContract.setAdditionalCost(FormUtil.readInt("Additional Costs"));
			tenancyContract.setDuration(FormUtil.readInt("Duration of rent (months)"));
			tenancyContract.setStartDate(new Date(new java.util.Date().getTime()));
			tenancyContract.setOwner(FormUtil.readInt("Person ID - Owner"));
			tenancyContract.setRenter(FormUtil.readInt("Person ID - Renter"));
			return tenancyContract;
		}
	}

	private int readEstateType() {
		return FormUtil.readInt("Type (0=house, 1=apartment, 2=none of them)");
	}

	private int readAndPersistEstate(Estate estate, int type) {
		int savedObjectId = -1;
		readEstateProperties(estate);
		switch(type) {
			case 0:
				House house = readHouseProperties(estate);
				savedObjectId = accessHelper.save(house);
				break;
			case 1:
				Apartment apartment = readApartmentProperties(estate);
				savedObjectId = accessHelper.save(apartment);
				break;
			case 2:
				savedObjectId = accessHelper.save(estate);
				break;
			default: break;
		}
		return savedObjectId;
	}

}
