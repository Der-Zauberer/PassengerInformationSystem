package eu.derzauberer.pis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.repositories.Repository;

@Service
public class UserService {
	
	private final Repository<User> userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(Repository<User> userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public Optional<User> login(String username, String password) {
		return userRepository.getById(username).filter(user -> passwordEncoder.matches(password, user.getPasswordHash()));
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
		return passwordEncoder.encode(password);
	}
	
}
