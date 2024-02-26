package eu.derzauberer.pis.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.enums.UserRole;
import eu.derzauberer.pis.model.UserModel;
import eu.derzauberer.pis.persistence.EntityRepository;
import eu.derzauberer.pis.persistence.Lazy;

@Service
public class UserService extends EntityService<UserModel> {
	
	private final EntityRepository<UserModel> userRepository;
	private final PasswordEncoder passwordEncoder;
	private final Set<String> expiredSessions;
	
	@Autowired
	public UserService(EntityRepository<UserModel> userRepository, PasswordEncoder passwordEncoder) {
		super(userRepository);
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		expiredSessions = new HashSet<>();
		if (userRepository.isEmpty()) {
			final UserModel admin = new UserModel("admin", "Admin");
			admin.setPassword(hashPassword("admin"));
			admin.setRole(UserRole.ADMIN);
			save(admin);
		}
	}
	
	@Override
	public Optional<UserModel> getById(String id) {
		return super.getById(id).map(this::removeCredentials);
	}
	
	public Optional<UserModel> getByIdOrSecondaryId(String id) {
		return userRepository.getByIdOrSecondaryId(id).map(this::removeCredentials);
	}
	
	public Optional<UserModel> getByIdOrSecondaryIdWithPasswordHash(String id) {
		return userRepository.getByIdOrSecondaryId(id);
	}
	
	@Override
	public Collection<Lazy<UserModel>> getAll() {
		return super.getAll().stream().map(lazy -> lazy.map(this::removeCredentials)).toList();
	}
	
	@Override
	public UserModel save(UserModel entity) {
		expiredSessions.add(entity.getId());
		return super.save(entity);
	}
	
	@Override
	public boolean removeById(String id) {
		final boolean removed = super.removeById(id);
		if (removed) expiredSessions.add(id);
		return removed;
	}
	
	public String hashPassword(String password) {
		return passwordEncoder.encode(password);
	}
	
	public boolean matchPassword(String password, UserModel user) {
		if (user.getPassword() == null || user.getPassword().isEmpty()) return false;
		return passwordEncoder.matches(password, user.getPassword());
	}
	
	public boolean hasExpiredSession(String userId) {
		return expiredSessions.remove(userId);
	}
	
	private UserModel removeCredentials(UserModel user) {
		user.setPassword(null);
		return user;
	}
	
}
