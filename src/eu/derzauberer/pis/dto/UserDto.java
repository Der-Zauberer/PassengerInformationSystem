package eu.derzauberer.pis.dto;

import java.time.LocalDateTime;
import java.util.Set;

import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.model.User;

public class UserDto implements Entity<User> {

	private String id;
	private String name;
	private String email;
	private boolean enabled;
	private boolean passwordChangeRequired;
	private LocalDateTime created;
	private LocalDateTime lastLogin;
	private Set<String> roles;
	
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
	
	public LocalDateTime getCreated() {
		return created;
	}
	
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
	
	public LocalDateTime getLastLogin() {
		return lastLogin;
	}
	
	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	public Set<String> getRoles() {
		return roles;
	}
	
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
}
