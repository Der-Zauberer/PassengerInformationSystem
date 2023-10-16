package eu.derzauberer.pis.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import eu.derzauberer.pis.service.AuthenticationService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/studio/**").authenticated()
				.anyRequest().permitAll()
			)
			.authenticationProvider(authenticationService)
			.formLogin(form -> form
				.loginPage("/login")
				.permitAll()
				.successHandler(authenticationService)
			)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.logoutSuccessHandler(authenticationService)
			).rememberMe(rememberMe -> rememberMe
				.key("remember-me-key")
			);
		return http.build();
	}

}
