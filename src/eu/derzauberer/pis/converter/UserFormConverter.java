package eu.derzauberer.pis.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.dto.UserForm;
import eu.derzauberer.pis.structure.model.UserModel;

@Component
public class UserFormConverter implements FormConverter<UserModel, UserForm> {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserForm convertToForm(UserModel user) {
		final UserForm userForm = new UserForm();
		userForm.setId(user.getId());
		userForm.setName(user.getName());
		userForm.setEmail(user.getEmail());
		userForm.setEnabled(user.isEnabled());
		userForm.setPasswordChangeRequired(user.isPasswordChangeRequired());
		userForm.setRole(user.getRole());
		return userForm;
	}

	@Override
	public UserModel convertToModel(UserForm userForm) {
		final UserModel user = new UserModel(userForm.getId(), userForm.getName());
		return convertToModel(user, userForm);
	}

	@Override
	public UserModel convertToModel(UserModel user, UserForm userForm) {
		user.setName(userForm.getName());
		user.setEmail(userForm.getEmail());
		user.setEnabled(userForm.isEnabled());
		if (userForm.getPassword() != null) user.setPassword(passwordEncoder.encode(userForm.getPassword()));
		user.setPasswordChangeRequired(userForm.isPasswordChangeRequired());
		user.setRole(userForm.getRole());
		return user;
	}

}
