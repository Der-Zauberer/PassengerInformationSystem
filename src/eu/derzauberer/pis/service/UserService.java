package eu.derzauberer.pis.service;

import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.util.MemoryRepository;
import eu.derzauberer.pis.util.Service;

public class UserService extends Service<User> {

	public UserService() {
		super("users", new MemoryRepository<>("users", User.class));
	}

}
