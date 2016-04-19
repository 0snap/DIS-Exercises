package de.dis2011;


import de.dis2011.data.EstateAgent;

public class Main {

	// never changing constants back and quit
	private static final int BACK = 0;
	private static final int QUIT = 0;

	private static final int MENU_ESTATE_AGENTS = 1;

	// commands inside esatate-agent menu:
	private static final int NEW_ESTATE_AGENT = 1;
	private static final int EDIT_ESTATE_AGENT = 2;


	public static void main(String[] args) {
		showMainMenu();
	}

	public static void showMainMenu() {

		Menu mainMenu = new Menu("Main menu");
		mainMenu.addEntry("EstateAgent Administration", MENU_ESTATE_AGENTS);
		mainMenu.addEntry("Quit!", QUIT);
		
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_ESTATE_AGENTS:
					showEstateAgentMenu();
					break;
				case QUIT:
					return;
			}
		}
	}
	
	public static void showEstateAgentMenu() {

		Menu maklerMenu = new Menu("EstateAgent Administration");
		maklerMenu.addEntry("New EstateAgent", NEW_ESTATE_AGENT);
		maklerMenu.addEntry("Edit EstateAgent", EDIT_ESTATE_AGENT);
		maklerMenu.addEntry("Back to main menu", BACK);
		
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_ESTATE_AGENT:
					newEstateAgent();
					break;
				case EDIT_ESTATE_AGENT:
					editEstateAgent();
					break;
				case BACK:
					return;
			}
		}
	}
	
	public static void newEstateAgent() {
		EstateAgent agent = new EstateAgent();
		
		agent.setName(FormUtil.readString("Name"));
		agent.setAddress(FormUtil.readString("Adresse"));
		agent.setLogin(FormUtil.readString("Login"));
		agent.setPassword(FormUtil.readString("Passwort"));
		int id = agent.save();
		
		System.out.println("EstateAgent with ID " + id + " saved successfully.");
	}

	public static void editEstateAgent() {

		int id = Integer.parseInt(FormUtil.readString("ID to edit"));

		EstateAgent agent = EstateAgent.load(id);


		if (!FormUtil.readString("Passwort bestätigen").equals(agent.getPassword())) {
			System.out.println("Incorrect password! You are not allowed to edit this agent");
			return;
		}
		// else
		agent.setName(FormUtil.readString("Name"));
		agent.setAddress(FormUtil.readString("Adresse"));
		agent.setLogin(FormUtil.readString("Login"));
		agent.setPassword(FormUtil.readString("Passwort"));
		agent.save();
	}
}
