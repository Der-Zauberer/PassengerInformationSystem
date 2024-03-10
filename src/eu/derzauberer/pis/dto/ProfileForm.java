package eu.derzauberer.pis.dto;

import eu.derzauberer.pis.model.UserModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileForm {
	
	@NotBlank
	private String id;
	
	@NotBlank
	private String name;
	
	@Email
	private String email;
	
	public ProfileForm(UserModel user) {
		id = user.getId();
		name = user.getName();
		email = user.getEmail();
	}
	
	public UserModel toUserModel(UserModel existingUser) {
		existingUser.setName(name);
		existingUser.setEmail(email);
		return existingUser;
	}

}
