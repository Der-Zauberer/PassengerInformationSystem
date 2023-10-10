package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class User implements Entity<User>, NameEntity, UserDetails {
	
	private final String id;
	private String name;
	private String mail;
	private String password;
	private boolean enabled;
	private boolean locked;
	private boolean expired;
	private boolean credentialsExpired;
	private LocalDateTime created;
	private LocalDateTime lastLogin;
	private final Set<String> permissions;
	
	@ConstructorProperties({ "id", "name", "password" })
	public User(String id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.enabled = true;
		this.locked = false;
		this.expired = false;
		this.credentialsExpired = false;
		created = LocalDateTime.now();
		this.permissions = new HashSet<>();
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getUsername() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public String getMail() {
		return mail;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}
	
	public void setAccountLocked(boolean locked) {
		this.locked = locked;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return !expired;
	}
	
	public void setAccountExpired(boolean expired) {
		this.expired = expired;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return !credentialsExpired;
	}
	
	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
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
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		final Set<GrantedAuthority> authorities = new HashSet<>();
		permissions.forEach(permission -> authorities.add(() -> permission));
		return authorities;
	}
	
}
