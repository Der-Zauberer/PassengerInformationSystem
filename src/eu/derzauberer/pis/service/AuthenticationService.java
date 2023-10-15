package eu.derzauberer.pis.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.User;

@Service
public class AuthenticationService implements UserDetailsService, AuthenticationProvider {

	private final UserService userService;
	
	public AuthenticationService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return convertUserDetails(userService.getByIdOrEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username not found for \"" + username + "\"")));
	}
	
	@Override
	@SuppressWarnings("serial")
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
		final User user = userService.getByIdOrEmail(username).orElse(null);
		if (user != null && user.isEnabled() && userService.matchPassword(password, user)) {
	        return new UsernamePasswordAuthenticationToken(username, password, convertGrantedAuthorities(user.getRoles()));
	    }
		throw new AuthenticationException("Your credentials aren't correct, please try again!") {};
	}
	
	@SuppressWarnings("serial")
	private UserDetails convertUserDetails(User user) {
		return new UserDetails() {
			@Override public boolean isEnabled() { return !user.isEnabled(); }
			@Override public boolean isCredentialsNonExpired() { return true; }
			@Override public boolean isAccountNonLocked() {	return true; }
			@Override public boolean isAccountNonExpired() { return true; }
			@Override public String getUsername() { return user.getId(); }
			@Override public String getPassword() { return user.getPassword(); }
			@Override public Collection<? extends GrantedAuthority> getAuthorities() { return convertGrantedAuthorities(user.getRoles()); }
		};
	}
	
	private Collection<? extends GrantedAuthority> convertGrantedAuthorities(Set<String> roles) {
		final Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		roles.forEach(role -> grantedAuthorities.add(() -> role));
		return grantedAuthorities;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
	
}
