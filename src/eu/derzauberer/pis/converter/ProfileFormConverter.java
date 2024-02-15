package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.dto.ProfileForm;
import eu.derzauberer.pis.model.UserModel;

@Component
public class ProfileFormConverter implements FormConverter<UserModel, ProfileForm> {

	@Override
	public ProfileForm convertToForm(UserModel user) {
		final ProfileForm profileForm = new ProfileForm();
		profileForm.setId(user.getId());
		profileForm.setName(user.getName());
		profileForm.setEmail(user.getEmail());
		return profileForm;
	}

	@Override
	public UserModel convertToModel(ProfileForm userForm) {
		final UserModel user = new UserModel(userForm.getId(), userForm.getName());
		return convertToModel(user, userForm);
	}

	@Override
	public UserModel convertToModel(UserModel user, ProfileForm userForm) {
		user.setName(userForm.getName());
		user.setEmail(userForm.getEmail());
		return user;
	}

}
