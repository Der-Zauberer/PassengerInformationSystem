package eu.derzauberer.pis.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.repositories.MemoryRepository;
import eu.derzauberer.pis.repositories.Repository;
import eu.derzauberer.pis.util.SearchTree;

@Service
public class StationService {
	
	private final Repository<Station> stationRepository = new MemoryRepository<>("stations", Station.class);
	private final SearchTree<Station> search = new SearchTree<>(stationRepository);
	
	public StationService() {
		stationRepository.getList().forEach(search::add);
	}
	
	public void add(Station station) {
		stationRepository.add(station);
		search.remove(station);
		search.add(station);
	}
	
	public boolean removeById(String stationId) {
		search.removeById(stationId);
		return stationRepository.removeById(stationId);
	}
	
	public boolean containsById(String stationId) {
		return stationRepository.containsById(stationId);
	}
	
	public Optional<Station> getById(String stationId) {
		return stationRepository.getById(stationId);
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
	
	public List<Station> getStations() {
		return stationRepository.getList();
	}
	
	public int size() {
		return stationRepository.size();
	}

}
