package eu.derzauberer.pis.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.components.IdentificationComponent;
import eu.derzauberer.pis.components.SearchComponent;
import eu.derzauberer.pis.entity.User;
import eu.derzauberer.pis.enums.UserRole;
import eu.derzauberer.pis.repository.EntityRepository;
import eu.derzauberer.pis.util.Collectable;

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
	public void save(User entity) {
		if (entity.getEmail() != null) {
			getById(entity.getEmail()).ifPresent(existing -> {
				if (!existing.getId().equals(entity.getId())) {
					throw new IllegalArgumentException("Identification email " + entity.getEmail() + " already exists as id!");
				}
			});
			emailIdentification.getByIdentification(entity.getEmail()).ifPresent(existing -> {
				if (!existing.getId().equals(entity.getId())) {
					throw new IllegalArgumentException("Identification email " + entity.getEmail() + " already exists as email!");
				}
			});
		}
		if (entity.getId() != null) {
			emailIdentification.getByIdentification(entity.getId()).ifPresent(existing -> {
				if (!existing.getId().equals(entity.getId())) {
					throw new IllegalArgumentException("Identification id " + entity.getId() + " already exists as email!");
				}
			});
		}
		super.save(entity);
	}
	
	@Override
	public Optional<User> getById(String id) {
		return super.getById(id).or(() -> emailIdentification.getByIdentification(id));
	}
	
	public Collectable<User> search(String search) {
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
