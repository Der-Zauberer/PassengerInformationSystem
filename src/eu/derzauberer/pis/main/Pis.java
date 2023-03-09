package eu.derzauberer.pis.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import eu.derzauberer.pis.model.Line;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.model.TrainOperator;
import eu.derzauberer.pis.model.TrainType;
import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.util.FileRepository;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"eu.derzauberer.pis.util"})
public class Pis {
	
	private static final FileRepository<User, String> userRepository = new FileRepository<>("users", User.class);
	private static final FileRepository<Station, String> stationRepository = new FileRepository<>("stations", Station.class);
	private static final FileRepository<TrainType, String> typeRepository = new FileRepository<>("types", TrainType.class);
	private static final FileRepository<TrainOperator, String> operatorRepository = new FileRepository<>("operators", TrainOperator.class);
	private static final FileRepository<Line, Long> lineRepository = new FileRepository<>("lines", Line.class);
	
	public static void main(String[] args) {
		SpringApplication.run(Pis.class, args);
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

}
