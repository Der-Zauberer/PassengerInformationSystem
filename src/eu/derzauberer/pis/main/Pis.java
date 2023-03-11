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
	
	private static final SpringConfiguration factory = new SpringConfiguration();
	private static final Command command = new Command("pis");
	
	private static final FileRepository<User, String> userRepository = new FileRepository<>("users", User.class);
	private static final FileRepository<Station, String> stationRepository = new FileRepository<>("stations", Station.class);
	private static final FileRepository<TrainType, String> typeRepository = new FileRepository<>("types", TrainType.class);
	private static final FileRepository<TrainOperator, String> operatorRepository = new FileRepository<>("operators", TrainOperator.class);
	private static final FileRepository<Line, Long> lineRepository = new FileRepository<>("lines", Line.class);
	
	private static final Map<String, FileRepository<?, ?>> repositories = new HashMap<>();
	
	public static void main(String[] args) {
		registerCommands();
		registerRepositories();
		if (args.length != 0) {
			command.executeCommand(command.getName(), args);
			return;
		}
		SpringApplication.run(Pis.class, args);
	}
	
	private static void registerCommands() {
		command.registerSubCommand(new ExtractCommand());
		command.registerSubCommand(new PackageCommand());
	}
	
	private static void registerRepositories() {
		repositories.put("users", userRepository);
		repositories.put("stations", stationRepository);
		repositories.put("types", typeRepository);
		repositories.put("operators", operatorRepository);
		repositories.put("lines", lineRepository);
	}
	
	public static SpringConfiguration getFactory() {
		return factory;
	}
	
	public static Command getCommand() {
		return command;
	}
	
	public static FileRepository<User, String> getUserRepository() {
		return userRepository;
	}
	
	public static FileRepository<Station, String> getStationRepository() {
		return stationRepository;
	}
	
	public static FileRepository<TrainType, String> getTypeRepository() {
		return typeRepository;
	}
	
	public static FileRepository<TrainOperator, String> getOperatorRepository() {
		return operatorRepository;
	}
	
	public static FileRepository<Line, Long> getLineRepository() {
		return lineRepository;
	}
	
	public static Map<String, FileRepository<?, ?>> getRepositories() {
		return Collections.unmodifiableMap(repositories);
	}

}
