package de.derzauberer.pis.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import de.derzauberer.pis.model.Station;
import de.derzauberer.pis.util.FileRepository;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"eu.girc.pis.controller", "eu.girc.pis.utils"})
public class Pis {
	
	private static final FileRepository<Station, String> stationRepository = new FileRepository<>("stations", ".json");
	
	public static void main(String[] args) {
		stationRepository.add(new Station("Singen_Hohentwiel", "Singen (Hohentwiel)"));
		SpringApplication.run(Pis.class, args);
	}
	
	public static FileRepository<Station, String> getStationRepository() {
		return stationRepository;
	}

}
