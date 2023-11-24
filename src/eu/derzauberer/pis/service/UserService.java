package eu.derzauberer.pis.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.components.IdentificationComponent;
import eu.derzauberer.pis.components.SearchComponent;
import eu.derzauberer.pis.enums.UserRole;
import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.repository.EntityRepository;
import eu.derzauberer.pis.util.Result;

@Service
public class UserService extends EntityService<User> {
	
	private final PasswordEncoder passwordEncoder;
	private final IdentificationComponent<User> emailIdentification;
	private final SearchComponent<User> search;
	
	@Autowired
	public UserService(EntityRepository<User> userRepository, PasswordEncoder passwordEncoder) {
		super(userRepository);
		this.passwordEncoder = passwordEncoder;
		this.search = new SearchComponent<>(this);
		this.emailIdentification = new IdentificationComponent<>(this, User::getEmail);
		if (isEmpty()) {
			final User admin = new User("admin", "Admin");
			admin.setPassword(hashPassword("admin"));
			admin.setRole(UserRole.ADMIN);
			save(admin);
		}
	}
	
	@Override
	public User save(User user) {
		if (user.getEmail() != null) {
			getById(user.getEmail()).ifPresent(existing -> {
				if (!existing.getId().equals(user.getId())) {
					throw new IllegalArgumentException("Identification email " + user.getEmail() + " already exists as id!");
				}
			});
			emailIdentification.getByIdentification(user.getEmail()).ifPresent(existing -> {
				if (!existing.getId().equals(user.getId())) {
					throw new IllegalArgumentException("Identification email " + user.getEmail() + " already exists as email!");
				}
			});
		}
		if (user.getId() != null) {
			emailIdentification.getByIdentification(user.getId()).ifPresent(existing -> {
				if (!existing.getId().equals(user.getId())) {
					throw new IllegalArgumentException("Identification id " + user.getId() + " already exists as email!");
				}
			});
		}
		return super.save(user);
	}
	
	@Override
	public Optional<User> getById(String id) {
		return super.getById(id).or(() -> emailIdentification.getByIdentification(id));
	}
	
	public Result<User> search(String search) {
		return this.search.search(search);
	}
	
	public String hashPassword(String password) {
		return passwordEncoder.encode(password);
	}
	
	public boolean matchPassword(String password, User user) {
		if (user.getPassword() == null || user.getPassword().isEmpty()) return false;
		return passwordEncoder.matches(password, user.getPassword());
	}
	
}
