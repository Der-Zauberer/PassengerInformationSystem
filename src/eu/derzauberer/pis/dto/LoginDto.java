package eu.derzauberer.pis.dto;

import java.beans.ConstructorProperties;

public class LoginDto {
	
	private final String username;
	private final String password;
	
	@ConstructorProperties({ "username", "password" })
	public LoginDto(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public static LoginDto empty() {
		return new LoginDto(null, null);
	}

}
