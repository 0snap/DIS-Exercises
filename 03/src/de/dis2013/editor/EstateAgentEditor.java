package de.dis2013.editor;

import de.dis2013.core.DataAccessService;
import de.dis2013.data.EstateAgent;
import de.dis2013.menu.AgentSelectionMenu;
import de.dis2013.menu.Menu;
import de.dis2013.util.FormUtil;

public class EstateAgentEditor {
	private DataAccessService service;
	
	public EstateAgentEditor(DataAccessService service) {
		this.service = service;
	}
	
	public void showAgentMenu() {
		final int NEW_AGENT = 0;
		final int EDIT_AGENT = 1;
		final int DELETE_AGENT = 2;
		final int BACK = 3;
		
		Menu agentMenu = new Menu("EstateAgent-Verwaltung");
		agentMenu.addEntry("New EstateAgent", NEW_AGENT);
		agentMenu.addEntry("Edit EstateAgent", EDIT_AGENT);
		agentMenu.addEntry("Delete EstateAgent", DELETE_AGENT);
		agentMenu.addEntry("Back", BACK);
		
		while(true) {
			int response = agentMenu.show();
			
			switch(response) {
				case NEW_AGENT:
					newAgent();
					break;
				case EDIT_AGENT:
					editAgent();
					break;
				case DELETE_AGENT:
					deleteAgent();
					break;
				case BACK:
					return;
			}
		}
	}
	
	public void newAgent() {
		EstateAgent agent = new EstateAgent();
		
		agent.setName(FormUtil.readString("Name"));
		agent.setAddress(FormUtil.readString("Adresse"));
		agent.setLogin(FormUtil.readString("Login"));
		agent.setPassword(FormUtil.readString("Passwort"));
		service.persist(agent);
		
		System.out.println("Created EstateAgent with ID "+agent.getId()+".");
	}
	

	public void editAgent() {
		Menu agentSelectionMenu = new AgentSelectionMenu("Edit Agent", service.getAllAgents());
		int id = agentSelectionMenu.show();

		if(id != AgentSelectionMenu.BACK) {
			EstateAgent agent = (EstateAgent)service.getById(EstateAgent.class, id);
			System.out.println("Edit EstateAgent "+agent.getName()+". Leave empty for no changes");
			
			String new_name = FormUtil.readString("Name ("+agent.getName()+")");
			String new_address = FormUtil.readString("Address ("+agent.getAddress()+")");
			String new_login = FormUtil.readString("Login ("+agent.getLogin()+")");
			String new_password = FormUtil.readString("Password ("+agent.getPassword()+")");
			
			if(!new_name.equals(""))
				agent.setName(new_name);
			if(!new_address.equals(""))
				agent.setAddress(new_address);
			if(!new_login.equals(""))
				agent.setLogin(new_login);
			if(!new_password.equals(""))
				agent.setPassword(new_password);
			service.update(agent);
		}
	}
	
	/**
	 * Löscht einen EstateAgent, nachdem der Benutzer
	 * ihn ausgewählt hat.
	 */
	public void deleteAgent() {
		Menu agentSelectionMenu = new AgentSelectionMenu("Delete EstateAgent", service.getAllAgents());
		int id = agentSelectionMenu.show();

		if(id != AgentSelectionMenu.BACK) {
			EstateAgent agent = (EstateAgent)service.getById(EstateAgent.class, id);
			service.delete(agent);
		}
	}
}
