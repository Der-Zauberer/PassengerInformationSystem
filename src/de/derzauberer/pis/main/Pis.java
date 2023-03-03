package de.derzauberer.pis.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import de.derzauberer.pis.model.Station;
import de.derzauberer.pis.util.FileRepository;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"de.derzauberer.pis.util"})
public class Pis {
	
	private static final FileRepository<Station, String> stationRepository = new FileRepository<>("stations", Station.class);
	
	public static void main(String[] args) {
		SpringApplication.run(Pis.class, args);
	}
	
	public static FileRepository<Station, String> getStationRepository() {
		return stationRepository;
	}

}
