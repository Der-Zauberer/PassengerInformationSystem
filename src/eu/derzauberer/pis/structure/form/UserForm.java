package eu.derzauberer.pis.structure.form;

import eu.derzauberer.pis.structure.enums.UserRole;

public class UserForm {
	
	private String id;
	private String name;
	private String email;
	private String password;
	private boolean enabled;
	private boolean passwordChangeRequired;
	private UserRole role;
	
	public UserForm() {
		enabled = false;
		passwordChangeRequired = false;
		role = UserRole.USER;
	}
	
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
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
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
