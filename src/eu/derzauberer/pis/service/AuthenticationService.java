package eu.derzauberer.pis.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthenticationService extends SavedRequestAwareAuthenticationSuccessHandler implements UserDetailsService, AuthenticationProvider, AuthenticationSuccessHandler, LogoutSuccessHandler {

	private final UserService userService;
	
	@Autowired
	public AuthenticationService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return convertUserDetails(userService.getById(username).orElseThrow(() -> new UsernameNotFoundException("Username not found for \"" + username + "\"")));
	}
	
	@Override
	@SuppressWarnings("serial")
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
		final User user = userService.getById(username).orElse(null);
		if (user != null && user.isEnabled() && userService.matchPassword(password, user)) {
			user.setLastLogin(LocalDateTime.now());
			userService.save(user);
	        return new UsernamePasswordAuthenticationToken(username, password, convertGrantedAuthorities(user.getRoles()));
	    }
		throw new AuthenticationException("Your credentials aren't correct, please try again!") {};
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		final HttpSession session = request.getSession();
		if (session != null) {
			session.setAttribute("authenticated", true);
			userService.getById(authentication.getName()).ifPresent(user -> {
				session.setAttribute("id", user.getId());
				session.setAttribute("name", user.getName());
				session.setAttribute("email", user.getEmail());
			});
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		final HttpSession session = request.getSession(false);
		if (session != null) {
			session.setAttribute("authenticated", false);
			session.removeAttribute("id");
			session.removeAttribute("name");
			session.removeAttribute("email");
		}
		response.sendRedirect("/");
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
