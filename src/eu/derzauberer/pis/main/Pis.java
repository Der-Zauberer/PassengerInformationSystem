package eu.derzauberer.pis.main;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"eu.derzauberer.pis.util"})
public class Pis {
	
	private static final Command command = new Command("pis");
	private static final SpringConfiguration springConfiguration = new SpringConfiguration();
	private static final Map<String, FileRepository<?, ?>> repositories = new HashMap<>();
	
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
	
	public static Command getCommand() {
		return command;
	}
	
	public static SpringConfiguration getSpringConfiguration() {
		return springConfiguration;
	}
	
	public static Map<String, FileRepository<?, ?>> getRepositories() {
		return Collections.unmodifiableMap(repositories);
	}

}
