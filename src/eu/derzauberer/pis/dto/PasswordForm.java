package eu.derzauberer.pis.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordForm {
	
	@NotBlank
	private String oldPassword;
	
	@NotBlank
	private String newPassword;

}
