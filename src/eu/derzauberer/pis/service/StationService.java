package eu.derzauberer.pis.service;

import java.util.Collections;
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
		getRepository().setRemoveAction(search::removeById);
		getRepository().setUpdateAction(entity -> { search.remove(entity); search.add(entity); });
		getRepository().getList().forEach(search::add);
	}
	
	public List<Station> searchByName(String name) {
		final List<Station> results = search.searchByName(name);
		Collections.sort(results, (station1, station2) -> {
			final boolean station1startsWithName = station1.getName().toLowerCase().startsWith(name.toLowerCase());
			final boolean station2startsWithName = station2.getName().toLowerCase().startsWith(name.toLowerCase());
			if (station1startsWithName && !station2startsWithName) return -1;
			if (!station1startsWithName && station2startsWithName) return 1;
			final int station1platforms = station1.getPlatforms() != null && !station1.getPlatforms().isEmpty() ? station1.getPlatforms().size() : 1;
			final int station2platforms = station2.getPlatforms() != null && !station2.getPlatforms().isEmpty() ? station2.getPlatforms().size() : 1;
			if (station1platforms > station2platforms) return -1;
			if (station1platforms < station2platforms) return 1;
			return 0;
		});
		return results;
	}

}
