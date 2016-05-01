package de.dis2013.menu;


import de.dis2013.data.EstateAgent;

import java.util.List;

public class AgentSelectionMenu extends Menu {
	public static final int BACK = -1;
	
	public AgentSelectionMenu(String title, List<EstateAgent> agents) {
		super(title);
		for(EstateAgent agent : agents) {
			addEntry(agent.getName(), agent.getId());
		}
		addEntry("Back", BACK);
	}
}
