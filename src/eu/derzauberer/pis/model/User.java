package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class User implements Entity<String> {
	
	private final String email;
	private String name;
	private String password;
	private boolean disabled = false;
	private boolean forcePasswordChange;
	private LocalDateTime created;
	private LocalDateTime lastLogin;
	private final Set<String> permissions;
	
	@ConstructorProperties({"email", "name", "password"})
	public User(String email, String username, String password) {
		this.email = email;
		this.name = username;
		this.password = password;
		this.disabled = false;
		this.forcePasswordChange = false;
		created = LocalDateTime.now();
		this.permissions = new HashSet<>();
	}
	
	@Override
	public String getId() {
		return email;
	}
	
	public String getEmail() {
		return email;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isDisabled() {
		return disabled;
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public boolean hasForcePasswordChange() {
		return forcePasswordChange;
	}
	
	public void setForcePasswordChange(boolean forcePasswordChange) {
		this.forcePasswordChange = forcePasswordChange;
	}
	
	public LocalDateTime getCreated() {
		return created;
	}
	
	public LocalDateTime getLastLogin() {
		return lastLogin;
	}
	
	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	public Set<String> getPermissions() {
		return permissions;
	}
	
}
