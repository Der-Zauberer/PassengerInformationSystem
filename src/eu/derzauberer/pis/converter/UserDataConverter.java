package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.dto.UserData;
import eu.derzauberer.pis.model.UserModel;

@Component
public class UserDataConverter implements DataConverter<UserModel, UserData> {

	@Override
	public UserData convert(UserModel user) {
		final UserData userData = new UserData();
		userData.setId(user.getId());
		userData.setName(user.getName());
		userData.setEmail(user.getEmail());
		userData.setEnabled(user.isEnabled());
		userData.setPasswordChangeRequired(user.isPasswordChangeRequired());
		userData.setCreated(user.getCreated());
		userData.setLastLogin(user.getLastLogin());
		userData.setRole(user.getRole());
		return userData;
	}

}
