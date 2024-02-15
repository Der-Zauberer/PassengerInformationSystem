package eu.derzauberer.pis.dto;

import eu.derzauberer.pis.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {
	
	private String id;
	private String name;
	private String email;
	private String password;
	private boolean enabled = false;
	private boolean passwordChangeRequired = false;
	private UserRole role = UserRole.USER;

}
