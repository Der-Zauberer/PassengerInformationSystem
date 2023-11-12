package eu.derzauberer.pis.dto;

import eu.derzauberer.pis.enums.UserRole;

public class UserEditDto {
	
	private String id;
	private String name;
	private String email;
	private boolean enabled;
	private boolean passwordChangeRequired;
	private UserRole role;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isPasswordChangeRequired() {
		return passwordChangeRequired;
	}
	
	public void setPasswordChangeRequired(boolean passwordChangeRequired) {
		this.passwordChangeRequired = passwordChangeRequired;
	}
	
	public void setRole(UserRole role) {
		this.role = role;
	}
	
	public UserRole getRole() {
		return role;
	}

}
