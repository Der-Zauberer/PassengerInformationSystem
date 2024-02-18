package eu.derzauberer.pis.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.converter.DataConverter;
import eu.derzauberer.pis.dto.UserData;
import eu.derzauberer.pis.model.UserModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthenticationService implements AuthenticationProvider, UserDetailsService {

	private final UserService userService;
	private final DataConverter<UserModel, UserData> userDataConverter;
	
	@Autowired
	public AuthenticationService(UserService userService, DataConverter<UserModel, UserData> userDataConverter) {
		this.userService = userService;
		this.userDataConverter = userDataConverter;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return convertUserDetails(userService.getById(username).orElseThrow(() -> new UsernameNotFoundException("Username not found for \"" + username + "\"")));
	}
	
	@SuppressWarnings("serial")
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        if (username == null || username.isEmpty()) throw new IllegalArgumentException("Username must not be null or empty!");
		final UserModel user = userService.getByIdOrSecondaryId(username)
			.filter(UserModel::isEnabled)
			.filter(processingUser -> userService.matchPassword(password, processingUser))
			.orElseThrow(() -> new AuthenticationException("Your credentials aren't correct, please try again!") {});
		user.setLastLogin(LocalDateTime.now());
		userService.save(user);
        return new UsernamePasswordAuthenticationToken(user.getId(), null, user.getRole().getGrantedAuthorities());
	}
	
	public void createSession(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		final HttpSession session = request.getSession();
		if (session != null) {
			session.setAttribute("user", true);
			userService.getById(authentication.getName())
				.map(userDataConverter::convert)
				.ifPresent(user -> {
					session.setAttribute("user", user);
					if (user.isPasswordChangeRequired()) session.setAttribute("passwordChangeReqired", "true");
				});
		}
		new SavedRequestAwareAuthenticationSuccessHandler().onAuthenticationSuccess(request, response, authentication);
	}
	
    public void updateSession(HttpSession session) {
		if (session != null && session.getAttribute("user") != null && session.getAttribute("user") instanceof UserData) {
			final UserData oldUser = (UserData) session.getAttribute("user");
			if (userService.hasExpiredSession(oldUser.getId())) {
				final UserModel user = userService.getById(oldUser.getId()).orElse(null);
				if (user == null || !user.isEnabled()) {
					session.invalidate();
					return;
				} else {
					session.setAttribute("user", userDataConverter.convert(user));
					if (user.isPasswordChangeRequired()) session.setAttribute("passwordChangeReqired", "true");
					if (oldUser.getRole() != user.getRole()) {
						final Authentication auth = new UsernamePasswordAuthenticationToken(user.getId(), null, user.getRole().getGrantedAuthorities());
						SecurityContextHolder.getContext().setAuthentication(auth);
					}
				}
			}
		}
	}
	
	public void destroySession(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		final HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute("user");
		}
		response.sendRedirect("/");
	}
	
	@SuppressWarnings("serial")
	private UserDetails convertUserDetails(UserModel user) {
		return new UserDetails() {
			@Override public boolean isEnabled() { return !user.isEnabled(); }
			@Override public boolean isCredentialsNonExpired() { return true; }
			@Override public boolean isAccountNonLocked() {	return true; }
			@Override public boolean isAccountNonExpired() { return true; }
			@Override public String getUsername() { return user.getId(); }
			@Override public String getPassword() { return user.getPassword(); }
			@Override public Collection<? extends GrantedAuthority> getAuthorities() { return user.getRole().getGrantedAuthorities(); }
		};
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
	
}
