package eu.derzauberer.pis.main;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import eu.derzauberer.pis.configuration.SpringConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"eu.derzauberer.pis"})
public class Pis {
	
	private static final SpringApplication springApplication = new SpringApplication(Pis.class);
	
	public static void main(String[] args) throws IOException {
		final List<String> arguments = Arrays.stream(args).map(arg -> arg.toLowerCase()).toList();
		if (arguments.contains("--no-caching")) SpringConfiguration.caching = false;
		if (arguments.contains("--no-indexing")) SpringConfiguration.indexing = false;
		
		final Properties properties = new Properties();
		properties.put("server.error.include-message", "always");
		properties.put("spring.main.allow-bean-definition-overriding", "true");
		springApplication.setDefaultProperties(properties);
		
		springApplication.run();
	}

}
