package eu.derzauberer.pis.main;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import eu.derzauberer.pis.configuration.SpringConfiguration;
import eu.derzauberer.pis.configuration.UserConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"eu.derzauberer.pis"})
public class Pis {
	
	private static final SpringApplication springApplication = new SpringApplication(Pis.class);
	private static final SpringConfiguration springConfiguration = new SpringConfiguration();
	private static final UserConfiguration userConfiguration = new UserConfiguration();
	
	public static void main(String[] args) {
		if (args.length != 0) {
			return;
		}
		configureSpring();
		springApplication.run();
	}
	
	private static void configureSpring() {
		final Properties properties = new Properties();
		properties.put("server.error.include-message", "always");
		springApplication.setDefaultProperties(properties);
	}
	
	
	public static UserConfiguration getUserConfiguration() {
		return userConfiguration;
	}
	
	
	public static SpringConfiguration getSpringConfiguration() {
		return springConfiguration;
	}

}
