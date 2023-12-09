package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structrue.data.UserData;
import eu.derzauberer.pis.structure.model.User;

@Component
public class UserDataConverter implements DataConverter<User, UserData> {

	@Override
	public UserData convert(User user) {
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
