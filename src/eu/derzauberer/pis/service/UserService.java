package eu.derzauberer.pis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.enums.UserRole;
import eu.derzauberer.pis.model.UserModel;
import eu.derzauberer.pis.persistence.IdentificationIndex;
import eu.derzauberer.pis.persistence.Lazy;
import eu.derzauberer.pis.persistence.Repository;
import eu.derzauberer.pis.persistence.SearchIndex;

@Service
public class UserService extends EntityService<UserModel> {
	
	private final PasswordEncoder passwordEncoder;
	private final IdentificationIndex<UserModel> emailIdentification;
	private final SearchIndex<UserModel> search;
	
	@Autowired
	public UserService(Repository<UserModel> userRepository, PasswordEncoder passwordEncoder) {
		super(userRepository);
		this.passwordEncoder = passwordEncoder;
		this.search = new SearchIndex<>(this);
		this.emailIdentification = new IdentificationIndex<>(this, UserModel::getEmail);
		if (userRepository.isEmpty()) {
			final UserModel admin = new UserModel("admin", "Admin");
			admin.setPassword(hashPassword("admin"));
			admin.setRole(UserRole.ADMIN);
			save(admin);
		}
	}
	
	@Override
	public UserModel save(UserModel user) {
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
	public Optional<UserModel> getById(String id) {
		return super.getById(id).or(() -> emailIdentification.getByIdentification(id));
	}
	
	public List<Lazy<UserModel>> search(String search) {
		return this.search.search(search);
	}
	
	public String hashPassword(String password) {
		return passwordEncoder.encode(password);
	}
	
	public boolean matchPassword(String password, UserModel user) {
		if (user.getPassword() == null || user.getPassword().isEmpty()) return false;
		return passwordEncoder.matches(password, user.getPassword());
	}
	
}
