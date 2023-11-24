package eu.derzauberer.pis.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import eu.derzauberer.pis.form.UserForm;
import eu.derzauberer.pis.model.User;

@Component
public class UserFormConverter implements FormConverter<User, UserForm> {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserForm convertToForm(User user) {
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
	public User convertToModel(UserForm userForm) {
		final User user = new User(userForm.getId(), userForm.getName());
		return convertToModel(user, userForm);
	}

	@Override
	public User convertToModel(User user, UserForm userForm) {
		user.setName(userForm.getName());
		user.setEmail(userForm.getEmail());
		user.setEnabled(userForm.isEnabled());
		if (userForm.getPassword() != null) user.setPassword(passwordEncoder.encode(userForm.getPassword()));
		user.setPasswordChangeRequired(userForm.isPasswordChangeRequired());
		user.setRole(userForm.getRole());
		return user;
	}

}
