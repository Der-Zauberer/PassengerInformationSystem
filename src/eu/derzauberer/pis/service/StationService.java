package eu.derzauberer.pis.service;

import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.util.EntityService;
import eu.derzauberer.pis.util.MemoryRepository;

@Service
public class StationService extends EntityService<Station> {
	
	public StationService() {
		super("stations", new MemoryRepository<>("stations", Station.class));
	}

}
