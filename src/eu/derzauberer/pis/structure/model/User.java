package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

import eu.derzauberer.pis.structure.enums.UserRole;

public class User extends Entity<User> implements NameEntity {
	
	private final String id;
	private String name;
	private String email;
	private String password;
	private boolean enabled;
	private boolean passwordChangeRequired;
	private LocalDateTime created;
	private LocalDateTime lastLogin;
	private UserRole role;
	
	@ConstructorProperties({ "id", "name" })
	public User(String id, String name) {
		this.id = id;
		this.name = name;
		this.enabled = true;
		this.passwordChangeRequired = false;
		created = LocalDateTime.now();
		this.role = UserRole.USER;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
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
	
	public LocalDateTime getCreated() {
		return created;
	}
	
	public LocalDateTime getLastLogin() {
		return lastLogin;
	}
	
	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	public void setRole(UserRole role) {
		this.role = role;
	}
	
	public UserRole getRole() {
		return role;
	}
	
	@Override
	public User copy() {
		final User user = new User(this.id, this.name);
		user.email = this.email;
		user.password = this.password;
		user.enabled = this.enabled;
		user.passwordChangeRequired = this.passwordChangeRequired;
		user.created = this.created;
		user.lastLogin = this.lastLogin;
		user.role = this.role;
		return user;
	}
	
}
