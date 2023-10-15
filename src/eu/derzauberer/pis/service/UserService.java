package eu.derzauberer.pis.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.components.IdentificationComponent;
import eu.derzauberer.pis.components.SearchComponent;
import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.repositories.EntityRepository;

@Service
public class UserService extends EntityService<User> {
	
	private final EntityRepository<User> userRepository;
	private final PasswordEncoder passwordEncoder;
	private final IdentificationComponent<User> emailIdentification;
	private final SearchComponent<User> search;
	
	@Autowired
	public UserService(EntityRepository<User> userRepository, PasswordEncoder passwordEncoder) {
		super(userRepository);
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.search = new SearchComponent<>(this);
		this.emailIdentification = new IdentificationComponent<>(this, User::getEmail);
		if (isEmpty()) add(new User("admin", "Admin", hashPassword("admin")));
	}
	
	@Override
	public void add(User entity) {
		getById(entity.getEmail()).ifPresent(existing -> {
			if (!existing.getId().equals(entity.getId())) {
				throw new IllegalArgumentException("Identification email " + entity.getEmail() + " already exists as id!");
			}
		});
		emailIdentification.get(entity.getEmail()).ifPresent(existing -> {
			if (!existing.getId().equals(entity.getId())) {
				throw new IllegalArgumentException("Identification email " + entity.getEmail() + " already exists as email!");
			}
		});
		emailIdentification.get(entity.getId()).ifPresent(existing -> {
			if (!existing.getId().equals(entity.getId())) {
				throw new IllegalArgumentException("Identification id " + entity.getId() + " already exists as email!");
			}
		});
		super.add(entity);
	}
	
	public Optional<User> getByIdOrEmail(String id) {
		return super.getById(id).or(() -> emailIdentification.get(id));
	}
	
	public List<User> search(String search) {
		return this.search.search(search);
	}
	
	public Optional<User> login(String username, String password) {
		return userRepository.getById(username)
			.filter(processingUser -> passwordEncoder.matches(password, processingUser.getPassword()))
			.map(processingUser -> {
				processingUser.setLastLogin(LocalDateTime.now());
				add(processingUser);
				return processingUser;
			});
	}
	
	public String hashPassword(String password) {
		return passwordEncoder.encode(password);
	}
	
	public boolean matchPassword(String password, User user) {
		return passwordEncoder.matches(password, user.getPassword());
	}
	
}
