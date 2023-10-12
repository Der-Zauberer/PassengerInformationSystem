package eu.derzauberer.pis.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.repositories.EntityRepository;

@Service
public class AuthenticationService implements UserDetailsService {

	private final EntityRepository<User> userRepository;
	
	public AuthenticationService(EntityRepository<User> userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return convertUserDetails(userRepository.getById(username).orElseThrow(() -> new UsernameNotFoundException("Username not found for \"" + username + "\"")));
	}
	
	@SuppressWarnings("serial")
	private UserDetails convertUserDetails(User user) {
		return new UserDetails() {
			@Override public boolean isEnabled() { return true; }
			@Override public boolean isCredentialsNonExpired() { return true; }
			@Override public boolean isAccountNonLocked() {	return true; }
			@Override public boolean isAccountNonExpired() { return true; }
			@Override public String getUsername() { return user.getId(); }
			@Override public String getPassword() { return user.getPasswordHash(); }
			@Override public Collection<? extends GrantedAuthority> getAuthorities() { return convertGrantedAuthorities(user.getPermissions()); }
		};
	}
	
	private Collection<? extends GrantedAuthority> convertGrantedAuthorities(Set<String> permissions) {
		final Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		permissions.forEach(permission -> grantedAuthorities.add(() -> permission));
		return grantedAuthorities;
	}

}
