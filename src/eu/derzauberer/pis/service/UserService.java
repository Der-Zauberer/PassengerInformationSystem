package eu.derzauberer.pis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.enums.UserRole;
import eu.derzauberer.pis.model.UserModel;
import eu.derzauberer.pis.persistence.EntityRepository;
import eu.derzauberer.pis.persistence.Lazy;
import eu.derzauberer.pis.persistence.SearchIndex;

@Service
public class UserService extends EntityService<UserModel> {
	
	private final EntityRepository<UserModel> userRepository;
	private final PasswordEncoder passwordEncoder;
	private final SearchIndex<UserModel> search;
	
	@Autowired
	public UserService(EntityRepository<UserModel> userRepository, PasswordEncoder passwordEncoder) {
		super(userRepository);
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.search = new SearchIndex<>(this);
		if (userRepository.isEmpty()) {
			final UserModel admin = new UserModel("admin", "Admin");
			admin.setPassword(hashPassword("admin"));
			admin.setRole(UserRole.ADMIN);
			save(admin);
		}
	}
	
	public Optional<UserModel> getByIdOrSecondaryId(String id) {
		return userRepository.getByIdOrSecondaryId(id);
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
