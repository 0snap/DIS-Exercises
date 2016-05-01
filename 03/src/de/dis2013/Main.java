package de.dis2013;

import de.dis2013.authentication.AgentAuthenticator;
import de.dis2013.authentication.PropertiesFileAuthenticator;
import de.dis2013.core.DataAccessService;
import de.dis2013.editor.EstateEditor;
import de.dis2013.editor.EstateAgentEditor;
import de.dis2013.editor.PersonEditor;
import de.dis2013.editor.ContractEditor;
import de.dis2013.menu.Menu;

public class Main {
	private static DataAccessService service;


    public static void main(String[] args) {
		service = new DataAccessService();
		showMainMenu();
	}
	
	private static void showMainMenu() {
		final int MENU_AGENT = 0;
		final int MENU_PERSON= 1;
		final int MENU_ESTATE = 2;
		final int MENU_CONTRACT = 3;
		final int QUIT = 4;
		
		Menu mainMenu = new Menu("Main Menu");
		mainMenu.addEntry("EstateAgent-Menu", MENU_AGENT);
		mainMenu.addEntry("Person-Menu", MENU_PERSON);
		mainMenu.addEntry("Estate-Menu", MENU_ESTATE);
		mainMenu.addEntry("Contact-Menu", MENU_CONTRACT);
		mainMenu.addEntry("Quit", QUIT);
		
		PropertiesFileAuthenticator adminAuthenticator = new PropertiesFileAuthenticator("admin.properties");
		AgentAuthenticator agentAuthenticator = new AgentAuthenticator(service);
		
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_AGENT:
					if(adminAuthenticator.authenticate()) {
						EstateAgentEditor agentEditor = new EstateAgentEditor(service);
						agentEditor.showAgentMenu();
					}
					break;
				case MENU_PERSON:
					if(agentAuthenticator.authenticate()) {
						PersonEditor personEditor = new PersonEditor(service);
						personEditor.showPersonMenu();
					}
					break;
				case MENU_ESTATE:
					if(agentAuthenticator.authenticate()) {
						EstateEditor ie = new EstateEditor(service, agentAuthenticator.getLastAuthenticatedAgent());
						ie.showImmoMenu();
					}
					break;
				case MENU_CONTRACT:
					if(agentAuthenticator.authenticate()) {
						ContractEditor ve = new ContractEditor(service, agentAuthenticator.getLastAuthenticatedAgent());
						ve.showContractMenu();
					}
					break;
				case QUIT:
					return;
			}
		}
	}
}
