package eu.derzauberer.pis.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.repositories.Repository;

@Service
public class UserService extends EntityService<User> {
	
	private final Repository<User> userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(Repository<User> userRepository, PasswordEncoder passwordEncoder) {
		super(userRepository);
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		if (isEmpty()) add(new User("admin", "Admin", hashPassword("admin")));
	}
	
	public Optional<User> login(String username, String password) {
		return userRepository.getById(username)
			.filter(processingUser -> passwordEncoder.matches(password, processingUser.getPasswordHash()))
			.map(processingUser -> {
				processingUser.setLastLogin(LocalDateTime.now());
				add(processingUser);
				return processingUser;
			});
	}
	
	public String hashPassword(String password) {
		return passwordEncoder.encode(password);
	}
	
}
