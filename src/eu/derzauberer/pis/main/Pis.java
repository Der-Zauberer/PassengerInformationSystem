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
import eu.derzauberer.pis.service.LineService;
import eu.derzauberer.pis.service.OperatorService;
import eu.derzauberer.pis.service.StationService;
import eu.derzauberer.pis.service.TypeService;
import eu.derzauberer.pis.service.UserService;
import eu.derzauberer.pis.util.Command;
import eu.derzauberer.pis.util.Service;
import eu.derzauberer.pis.util.SpringConfiguration;
import eu.derzauberer.pis.util.UserConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"eu.derzauberer.pis.util"})
public class Pis {
	
	private static final SpringConfiguration springConfig = new SpringConfiguration();
	private static final UserConfiguration userConfig = new UserConfiguration();
	private static final Map<String, Service<?>> services = new HashMap<>();
	private static final Command command = new Command("pis");
	
	public static void main(String[] args) {
		registerRepositories();
		if (args.length != 0) {
			registerCommands();
			command.executeCommand(command.getName(), args);
			return;
		}
		SpringApplication.run(Pis.class, args);
	}
	
	private static void registerRepositories() {
		services.put("lines", new LineService());
		services.put("operators", new OperatorService());
		services.put("stations", new StationService());
		services.put("types", new TypeService());
		services.put("users", new UserService());
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
	
	public static Set<String> getServices() {
		return services.keySet();
	}
	
	public static Service<?> getService(String name) {
		final Service<?> service = services.get(name);
		return service;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Entity> Service<T> getService(String name, Class<T> type) {
		final Service<T> service = (Service<T>) services.get(name);
		return service;
	}
	
	public static Command getCommand() {
		return command;
	}

}
