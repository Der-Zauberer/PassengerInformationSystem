package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.dto.UserInfoData;
import eu.derzauberer.pis.model.UserModel;

@Component
public class UserInfoDataConverter implements DataConverter<UserModel, UserInfoData> {

	@Override
	public UserInfoData convert(UserModel user) {
		final UserInfoData userInfoData = new UserInfoData();
		userInfoData.setId(user.getId());
		userInfoData.setName(user.getName());
		return userInfoData;
	}

}
