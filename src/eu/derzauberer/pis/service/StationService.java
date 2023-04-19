package eu.derzauberer.pis.service;

import java.util.List;

import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.util.EntityService;
import eu.derzauberer.pis.util.MemoryRepository;
import eu.derzauberer.pis.util.SearchTree;

@Service
public class StationService extends EntityService<Station> {
	
	private final SearchTree<Station> search = new SearchTree<>(getRepository()); 
	
	public StationService() {
		super("stations", new MemoryRepository<>("stations", Station.class));
		getRepository().setAddAction(search::add);
		getRepository().getList().forEach(search::add);
	}
	
	public List<Station> searchByName(String name) {
		return search.searchByName(name);
	}

}
