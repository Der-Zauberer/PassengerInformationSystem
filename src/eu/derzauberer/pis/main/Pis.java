package eu.derzauberer.pis.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import eu.derzauberer.pis.commands.DownloadCommand;
import eu.derzauberer.pis.commands.ExtractCommand;
import eu.derzauberer.pis.commands.PackageCommand;
import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.model.Line;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.model.TrainOperator;
import eu.derzauberer.pis.model.TrainType;
import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.util.Command;
import eu.derzauberer.pis.util.Repository;
import eu.derzauberer.pis.util.SpringConfiguration;
import eu.derzauberer.pis.util.UserConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"eu.derzauberer.pis.util"})
public class Pis {
	
	private static final SpringConfiguration springConfig = new SpringConfiguration();
	private static final UserConfiguration userConfig = new UserConfiguration();
	private static final Map<String, Repository<?>> repositories = new HashMap<>();
	private static final Command command = new Command("pis");
	
	public static void main(String[] args) {
		registerRepositories();
		if (args.length != 0) {
			registerCommands();
			command.executeCommand(command.getName(), args);
			return;
		}
		repositories.values().forEach(repository -> repository.initialize());
		SpringApplication.run(Pis.class, args);
	}
	
	private static void registerRepositories() {
		repositories.put("users", new Repository<>("users", User.class, false));
		repositories.put("stations", new Repository<>("stations", Station.class, false));
		repositories.put("types", new Repository<>("types", TrainType.class, false));
		repositories.put("operators", new Repository<>("operators", TrainOperator.class, false));
		repositories.put("lines", new Repository<>("lines", Line.class, true));
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
		final Repository<?> repository = repositories.get(name);
		if (repository != null && !repository.isInitiaized()) repository.initialize();
		return repository;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Entity> Repository<T> getRepository(String name, Class<T> type) {
		final Repository<T> repository = (Repository<T>) repositories.get(name);
		if (repository != null && !repository.isInitiaized()) repository.initialize();
		return repository;
	}
	
	public static Command getCommand() {
		return command;
	}

}
