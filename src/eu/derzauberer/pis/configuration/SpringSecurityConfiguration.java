package eu.derzauberer.pis.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

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
			.formLogin((form) -> form
				.loginPage("/login")
				.permitAll()
			)
			.logout((logout) -> logout
				.logoutUrl("/logout")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
			);
		return http.build();
	}

}
