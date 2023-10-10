package eu.derzauberer.pis.dto;

import java.beans.ConstructorProperties;

public class PasswordDto {
	
	private final String oldPassword;
	private final String newPassword;
	
	@ConstructorProperties({ "oldPassword", "newPassword" })
	public PasswordDto(String oldPassword, String newPassword) {
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}
	
	public String getOldPassword() {
		return oldPassword;
	}
	
	public String getNewPassword() {
		return newPassword;
	}

	public static PasswordDto empty() {
		return new PasswordDto(null, null);
	}
	
}
