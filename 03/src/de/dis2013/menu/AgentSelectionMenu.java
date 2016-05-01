package de.dis2013.menu;

import java.util.Set;

import de.dis2013.data.EstateAgent;

public class AgentSelectionMenu extends Menu {
	public static final int BACK = -1;
	
	public AgentSelectionMenu(String title, Set<EstateAgent> agents) {
		super(title);
		for(EstateAgent agent : agents) {
			addEntry(agent.getName(), agent.getId());
		}
		addEntry("Back", BACK);
	}
}
