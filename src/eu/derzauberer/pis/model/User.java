package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class User implements Entity<User> {
	
	private final String username;
	private String displayName;
	private String mail;
	private String passwordHash;
	private boolean disabled = false;
	private boolean forcePasswordChange;
	private LocalDateTime created;
	private LocalDateTime lastLogin;
	private final Set<String> permissions;
	
	@ConstructorProperties({ "username", "displayName", "passwordHash" })
	public User(String username, String displayName, String passwordHash) {
		this.username = username;
		this.displayName = displayName;
		this.passwordHash = passwordHash;
		this.disabled = false;
		this.forcePasswordChange = false;
		created = LocalDateTime.now();
		this.permissions = new HashSet<>();
	}
	
	@Override
	public String getId() {
		return username;
	}
	
	public String getUserName() {
		return username;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getMail() {
		return mail;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String getPasswordHash() {
		return passwordHash;
	}
	
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
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
