package de.dis2011;


import de.dis2011.data.DataAccessHelper;
import de.dis2011.data.Estate;
import de.dis2011.data.EstateAgent;

public class Main {

	// never changing constants back and quit
	private static final int BACK = 0;
	private static final int QUIT = 0;

	private static final int MENU_ESTATE_AGENTS = 1;
	private static final int MENU_ESTATES = 2;

	// commands inside the menus:
	private static final int NEW = 1;
	private static final int EDIT = 2;
	private static final int DELETE = 3;

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
		int id = Integer.parseInt(FormUtil.readString("Login Agent ID"));
		EstateAgent agent = accessHelper.loadEstateAgent(id);

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

	public void newEstate() {
		EstateAgent agent = loginEstateAgent();
		if(agent == null) {
			return;
		}
		// looged in, create new estate of this agent
		Estate estate = new Estate();
		readEstateProperties(estate);
		estate.setEstateAgent(agent.getId());
		int id = accessHelper.save(estate);

		System.out.println("Estate with ID " + id + " saved successfully.");
	}

	public void editEstate() {
		EstateAgent agent = loginEstateAgent();
		if(agent == null) {
			return;
		}
		// looged in, query estate of this agent
		int id = Integer.parseInt(FormUtil.readString("Estate ID to edit"));
		Estate estate = accessHelper.loadEstate(id);
		if(estate.getEstateAgent() != agent.getId()) {
			// logged in agent does not own this estate, cannot edit!
			System.out.println("You do not own this estate, you cannot edit it.");
			return;
		}
		readEstateProperties(estate);
		accessHelper.save(estate);
	}

    public void deleteEstate() {
		EstateAgent agent = loginEstateAgent();
		if(agent == null) {
			return;
		}
		// looged in, query estate of this agent
        int id = Integer.parseInt(FormUtil.readString("Estate ID to delete"));

		Estate estate = accessHelper.loadEstate(id);
		if(estate.getEstateAgent() != agent.getId()) {
			// logged in agent does not own this estate, cannot delete!
			System.out.println("You do not own this estate, you cannot delete it.");
			return;
		}
        accessHelper.deleteEstate(id);

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


}
