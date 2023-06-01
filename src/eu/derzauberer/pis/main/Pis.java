package eu.derzauberer.pis.main;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"eu.derzauberer.pis"})
public class Pis {
	
	private static final SpringApplication springApplication = new SpringApplication(Pis.class);
	
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

}
