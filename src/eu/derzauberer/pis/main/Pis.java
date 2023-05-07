package eu.derzauberer.pis.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import eu.derzauberer.pis.commands.DownloadCommand;
import eu.derzauberer.pis.commands.ExtractCommand;
import eu.derzauberer.pis.commands.PackageCommand;
import eu.derzauberer.pis.configuration.SpringConfiguration;
import eu.derzauberer.pis.configuration.UserConfiguration;
import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.model.LineSceduled;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.model.TrainOperator;
import eu.derzauberer.pis.model.TrainType;
import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.repositories.FileRepository;
import eu.derzauberer.pis.repositories.Repository;
import eu.derzauberer.pis.util.Command;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"eu.derzauberer.pis"})
public class Pis {
	
	private static final SpringApplication springApplication = new SpringApplication(Pis.class);
	private static final SpringConfiguration springConfig = new SpringConfiguration();
	private static final UserConfiguration userConfig = new UserConfiguration();
	private static final Map<String, Supplier<Repository<?>>> repositories = new HashMap<>();
	private static final Command command = new Command("pis", "pis");
	
	public static void main(String[] args) {
		if (args.length != 0) {
			registerRepositories();
			registerCommands();
			command.executeCommand(command.getName(), args);
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
	
	private static void registerRepositories() {
		repositories.put("lines", () -> new FileRepository<>("lines", LineSceduled.class));
		repositories.put("operators", () -> new FileRepository<>("operators", TrainOperator.class));
		repositories.put("stations", () -> new FileRepository<>("stations", Station.class));
		repositories.put("types", () -> new FileRepository<>("types", TrainType.class));
		repositories.put("users", () -> new FileRepository<>("users", User.class));
	}
	
	private static void registerCommands() {
		command.registerSubCommand(new DownloadCommand());
		command.registerSubCommand(new ExtractCommand());
		command.registerSubCommand(new PackageCommand());
	}
	
	public static SpringConfiguration getSpringConfig() {
		return springConfig;
	}
	
	public static UserConfiguration getUserConfig() {
		return userConfig;
	}
	
	public static Set<String> getRepositories() {
		return repositories.keySet();
	}
	
	public static Repository<?> getRepository(String name) {
		final Repository<?> repository = repositories.get(name).get();
		return repository;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Entity<T>> Repository<T> getRepository(String name, Class<T> type) {
		final Repository<T> repository = (Repository<T>) repositories.get(name).get();
		return repository;
	}
	
	public static Command getCommand() {
		return command;
	}

}
