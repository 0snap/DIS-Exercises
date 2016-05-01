package de.dis2013.authentication;

import de.dis2013.core.DataAccessService;
import de.dis2013.data.EstateAgent;
import de.dis2013.util.FormUtil;

public class AgentAuthenticator implements Authenticator {
	private DataAccessService service;
	private EstateAgent lastAuthenticatedAgent;
	
	public AgentAuthenticator(DataAccessService service) {
		this.service = service;
	}
	
	public EstateAgent getLastAuthenticatedAgent() {
		return this.lastAuthenticatedAgent;
	}
	
	public boolean authenticate() {
		boolean ret;
		
		String login = FormUtil.readString("EstateAgent-Login");
		String password = FormUtil.readPassword("Password");
		
		EstateAgent m = service.getAgentByLogin(login);
		
		if(m == null)
			ret = false;
		else
			ret = password.equals(m.getPassword());
		
		lastAuthenticatedAgent = m;
		
		if(!ret)
			FormUtil.showMessage("Login or password incorrect!");
		
		return ret;
	}
}
