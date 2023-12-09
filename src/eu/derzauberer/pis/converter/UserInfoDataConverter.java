package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.data.UserInfoData;
import eu.derzauberer.pis.structure.model.User;

@Component
public class UserInfoDataConverter implements DataConverter<User, UserInfoData> {

	@Override
	public UserInfoData convert(User user) {
		final UserInfoData userInfoData = new UserInfoData();
		userInfoData.setId(user.getId());
		userInfoData.setName(user.getName());
		return userInfoData;
	}

}
