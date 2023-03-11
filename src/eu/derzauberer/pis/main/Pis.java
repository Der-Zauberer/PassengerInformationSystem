package eu.derzauberer.pis.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import eu.derzauberer.pis.commands.ExtractCommand;
import eu.derzauberer.pis.commands.PackageCommand;
import eu.derzauberer.pis.model.Line;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.model.TrainOperator;
import eu.derzauberer.pis.model.TrainType;
import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.util.Command;
import eu.derzauberer.pis.util.FileRepository;
import eu.derzauberer.pis.util.SpringConfiguration;
import eu.derzauberer.pis.util.UserConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"eu.derzauberer.pis.util"})
public class Pis {
	
	private static final SpringConfiguration springConfig = new SpringConfiguration();
	private static final UserConfiguration userConfig = new UserConfiguration();
	private static final Map<String, FileRepository<?, ?>> repositories = new HashMap<>();
	private static final Command command = new Command("pis");
	
	public static void main(String[] args) {
		registerRepositories();
		registerCommands();
		if (args.length != 0) {
			command.executeCommand(command.getName(), args);
			return;
		}
		repositories.values().forEach(repository -> repository.initialize());
		SpringApplication.run(Pis.class, args);
	}
	
	private static void registerRepositories() {
		repositories.put("users", new FileRepository<>("users", User.class));
		repositories.put("stations", new FileRepository<>("stations", Station.class));
		repositories.put("types", new FileRepository<>("types", TrainType.class));
		repositories.put("operators", new FileRepository<>("operators", TrainOperator.class));
		repositories.put("lines", new FileRepository<>("lines", Line.class));
	}
	
	private static void registerCommands() {
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
	
	public static FileRepository<?, ?> getRepository(String string) {
		final FileRepository<?, ?> repository = repositories.get(string);
		if (repository != null && !repository.isInitiaized()) repository.initialize();
		return repository;
	}
	
	public static Command getCommand() {
		return command;
	}

}
