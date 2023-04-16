package eu.derzauberer.pis.service;

import java.util.List;

import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.util.MemoryRepository;
import eu.derzauberer.pis.util.SearchTree;
import eu.derzauberer.pis.util.Service;

@org.springframework.stereotype.Service
public class StationService extends Service<Station> {
	
	final SearchTree<Station> search = new SearchTree<>();

	public StationService() {
		super("stations", new MemoryRepository<>("stations", Station.class));
		getList().forEach(search::add);
		search.remove(getById("singen_hohentwiel").get());
	}
	
	@Override
	public void add(Station entity) {
		super.add(entity);
		search.add(entity);
	}

	@Override
	public boolean removeById(String id) {
		
		return super.removeById(id);
	}
	
	public List<Station> search(String search) {
		return this.search.search(search);
	}

}
