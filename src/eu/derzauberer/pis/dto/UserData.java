package eu.derzauberer.pis.dto;

import java.time.LocalDateTime;

import eu.derzauberer.pis.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData {

	private String id;
	private String name;
	private String email;
	private boolean enabled;
	private boolean passwordChangeRequired;
	private LocalDateTime created;
	private LocalDateTime lastLogin;
	private UserRole role;
	
}
