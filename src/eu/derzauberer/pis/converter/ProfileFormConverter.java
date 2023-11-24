package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.form.ProfileForm;
import eu.derzauberer.pis.model.User;

@Component
public class ProfileFormConverter implements FormConverter<User, ProfileForm> {

	@Override
	public ProfileForm convertToForm(User user) {
		final ProfileForm profileForm = new ProfileForm();
		profileForm.setId(user.getId());
		profileForm.setName(user.getName());
		profileForm.setEmail(user.getEmail());
		return profileForm;
	}

	@Override
	public User convertToModel(ProfileForm userForm) {
		final User user = new User(userForm.getId(), userForm.getName());
		return convertToModel(user, userForm);
	}

	@Override
	public User convertToModel(User user, ProfileForm userForm) {
		user.setName(userForm.getName());
		user.setEmail(userForm.getEmail());
		return user;
	}

}
