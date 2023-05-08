package eu.derzauberer.pis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.repositories.MemoryRepository;
import eu.derzauberer.pis.repositories.Repository;

@Service
public class UserService {
	
	final Repository<User> userRepository = new MemoryRepository<>("types", User.class);
	
	private static final PasswordEncoder ENCODER = Pis.getSpringConfig().getPasswordEncoder();
	
	public Optional<User> login(String username, String password) {
		return userRepository.getById(username).filter(user -> ENCODER.matches(password, user.getPasswordHash()));
	}
	
	public void add(User user) {
		userRepository.add(user);
	}
	
	public boolean removeById(String username) {
		return userRepository.removeById(username);
	}
	
	public boolean containsById(String username) {
		return userRepository.containsById(username);
	}
	
	public Optional<User> getById(String username) {
		return userRepository.getById(username);
	}
	
	public List<User> getUsers() {
		return userRepository.getList();
	}
	
	public int size() {
		return userRepository.size();
	}
	
	public String hashPassword(String password) {
		return ENCODER.encode(password);
	}
	
}
