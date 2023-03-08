package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"email", "name", "password", "disabled", "forcePasswordChange", "permissions"})
public class User implements Entity<String> {
	
	private final String email;
	private String name;
	private String password;
	private boolean disabled = false;
	private boolean forcePasswordChange;
	private final Set<String> permissions;
	
	@ConstructorProperties({"id", "name", "password"})
	public User(String email, String username, String password) {
		this.email = email;
		this.name = username;
		this.password = password;
		this.disabled = false;
		this.forcePasswordChange = false;
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
	
	public Set<String> getPermissions() {
		return permissions;
	}
	
}
