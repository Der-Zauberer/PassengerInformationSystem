package eu.derzauberer.pis.service;

import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.util.MemoryRepository;
import eu.derzauberer.pis.util.Service;

@org.springframework.stereotype.Service
public class StationService extends Service<Station> {

	public StationService() {
		super("stations", new MemoryRepository<>("stations", Station.class));
	}

}
