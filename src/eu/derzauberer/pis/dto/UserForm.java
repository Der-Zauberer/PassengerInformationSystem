package eu.derzauberer.pis.dto;

import org.springframework.security.crypto.password.PasswordEncoder;

import eu.derzauberer.pis.configuration.SpringConfiguration;
import eu.derzauberer.pis.enums.UserRole;
import eu.derzauberer.pis.model.UserModel;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserForm {
	
	@NotBlank
	private String id;
	
	@NotBlank
	private String name;
	
	@Email
	private String email;
	
	@Nullable
	private String password = null;
	
	private boolean enabled = false;
	
	private boolean passwordChangeRequired = false;
	
	private UserRole role = UserRole.USER;
	
	private static final PasswordEncoder PASSWORD_ENCODER = SpringConfiguration.getBean(PasswordEncoder.class);
	
	public UserForm(UserModel user) {
		id = user.getId();
		name = user.getName();
		email = user.getEmail();
		enabled = user.isEnabled();
		passwordChangeRequired = user.isPasswordChangeRequired();
		role = user.getRole();
	}
	
	public UserModel toUserModel() {
		return toUserModel(new UserModel(id, name));
	}
	
	public UserModel toUserModel(UserModel existingUser) {
		existingUser.setName(name);
		existingUser.setEmail(email);
		if (password != null && !password.isBlank()) existingUser.setPassword(PASSWORD_ENCODER.encode(password));
		existingUser.setEnabled(enabled);
		existingUser.setPasswordChangeRequired(passwordChangeRequired);
		existingUser.setRole(role);
		return existingUser;
	}

}
