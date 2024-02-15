package eu.derzauberer.pis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordForm {
	
	private String oldPassword;
	private String newPassword;

}
