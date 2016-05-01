package de.dis2013.editor;

import de.dis2013.core.EstateService;
import de.dis2013.data.EstateAgent;
import de.dis2013.menu.AgentSelectionMenu;
import de.dis2013.menu.Menu;
import de.dis2013.util.FormUtil;

public class EstateAgentEditor {
	private EstateService service;
	
	public EstateAgentEditor(EstateService service) {
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
		EstateAgent m = new EstateAgent();
		
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		service.addAgent(m);
		
		System.out.println("EstateAgent mit der ID "+m.getId()+" wurde erzeugt.");
	}
	
	/**
	 * Berarbeitet einen EstateAgent, nachdem der Benutzer ihn ausgewählt hat
	 */
	public void editAgent() {
		Menu agentSelectionMenu = new AgentById(id);
			System.out.println("EstateAgent "+m.getName()+" wird bearbeitet. Leere Felder bleiben unverändert.");
			
			String new_name = FormUtil.readString("Name ("+m.getName()+")");
			String new_address = FormUtil.readString("Adresse ("+m.getAddress()+")");
			String new_login = FormUtil.readString("Login ("+m.getLogin()+")");
			String new_password = FormUtil.readString("Passwort ("+m.getPassword()+")");
			
			if(!new_name.equals(""))
				m.setName(new_name);
			if(!new_address.equals(""))
				m.setAddress(new_address);
			if(!new_login.equals(""))
				m.setLogin(new_login);
			if(!new_password.equals(""))
				m.setPassword(new_password);
		}
	}
	
	/**
	 * Löscht einen EstateAgent, nachdem der Benutzer
	 * ihn ausgewählt hat.
	 */
	public void deleteAgent() {
		Menu agentSelectionMenu = new AgentById(id);
			service.deleteAgent(m);
		}
	}
}
