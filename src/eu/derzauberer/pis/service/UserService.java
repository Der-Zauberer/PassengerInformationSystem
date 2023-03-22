package eu.derzauberer.pis.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.util.MemoryRepository;
import eu.derzauberer.pis.util.Service;

public class UserService extends Service<User> {
	
	private static final PasswordEncoder ENCODER = Pis.getSpringConfig().getPasswordEncoder();

	public UserService() {
		super("users", new MemoryRepository<>("users", User.class));
	}
	
	public Optional<User> login(String username, String password) {
		return getById(username).filter(user -> ENCODER.matches(password, user.getPasswordHash()));
	}
	
	public String hashPassword(String password) {
		return ENCODER.encode(password);
	}
	
}
