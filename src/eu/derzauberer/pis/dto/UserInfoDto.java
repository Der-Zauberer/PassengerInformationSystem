package eu.derzauberer.pis.dto;

import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.model.User;

public class UserInfoDto implements Entity<User> {
	
	private String username;
	private String displayName;
	
	@Override
	public String getId() {
		return username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
