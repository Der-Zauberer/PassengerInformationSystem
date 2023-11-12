package eu.derzauberer.pis.enums;

import java.util.Set;

public enum UserRole {
	
	ADMIN("entity.role.admin", Set.of()),
	DEVELOPER("entity.role.developer", Set.of()),
	MAINTAINER("entity.role.maintainer", Set.of()),
	USER("entity.role.user", Set.of()),
	GUEST("entity.role.guest", Set.of());
	
	private final String localization;
	private final Set<String> permissions;
	
	UserRole(String localization, Set<String> permissions) {
		this.localization = localization;
		this.permissions = permissions;
	}
	
	public String getLocalization() {
		return localization;
	}
	
	public Set<String> getPermissions() {
		return permissions;
	}
	
	public boolean hasPermission(String permission) {
		return permissions.contains(permission);
	}

}
