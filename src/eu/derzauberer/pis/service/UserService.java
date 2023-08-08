package eu.derzauberer.pis.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.repositories.Repository;
import eu.derzauberer.pis.util.SearchTree;

@Service
public class UserService extends EntityService<User> {
	
	private final Repository<User> userRepository;
	private final SearchTree<User> search;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(Repository<User> userRepository, PasswordEncoder passwordEncoder) {
		super(userRepository);
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		if (isEmpty()) add(new User("admin", "Admin", hashPassword("admin")));
		this.search = new SearchTree<>(userRepository);
		userRepository.getList().forEach(search::add);
	}
	
	@Override
	public void add(User user) {
		userRepository.add(user);
		search.remove(user);
		search.add(user);
	}
	
	@Override
	public boolean removeById(String userId) {
		search.removeById(userId);
		return userRepository.removeById(userId);
	}
	
	public List<User> searchByName(String name) {
		return search.searchByName(name);
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
