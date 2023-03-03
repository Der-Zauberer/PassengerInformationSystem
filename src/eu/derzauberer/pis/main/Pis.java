package eu.derzauberer.pis.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.util.FileRepository;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"eu.derzauberer.pis.util"})
public class Pis {
	
	private static final FileRepository<Station, String> stationRepository = new FileRepository<>("stations", Station.class);
	
	public static void main(String[] args) {
		SpringApplication.run(Pis.class, args);
	}
	
	public static FileRepository<Station, String> getStationRepository() {
		return stationRepository;
	}

}
