package eu.derzauberer.pis.service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.SortedSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.persistence.EntityRepository;
import eu.derzauberer.pis.persistence.SearchIndex;
import eu.derzauberer.pis.structure.model.StationModel;
import eu.derzauberer.pis.structure.model.StationTrafficModel;
import eu.derzauberer.pis.structure.model.StationTrafficEntryModel;
import eu.derzauberer.pis.util.Result;
import eu.derzauberer.pis.util.SearchComparator;

@Service
public class StationService extends EntityService<StationModel> {
	
	private final EntityRepository<StationTrafficModel> stationTrafficRepository;
	private final SearchIndex<StationModel> searchComponent;
	
	@Autowired
	public StationService(EntityRepository<StationModel> stationRepository, EntityRepository<StationTrafficModel> stationTrafficRepository) {
		super(stationRepository);
		this.stationTrafficRepository = stationTrafficRepository;
		this.searchComponent = new SearchIndex<>(this, getStationSearchComperator());
	}
	
	public Result<StationModel> search(String search) {
		return searchComponent.search(search);
	}
	
	public SortedSet<StationTrafficEntryModel> getDeparturesInHour(String stationId, LocalDateTime dateTime) {
		final StationTrafficModel stationTraffic = 
				stationTrafficRepository.getById(StationTrafficModel.createIdFromNameAndDate(stationId, dateTime.toLocalDate()))
				.orElse(new StationTrafficModel(stationId, dateTime.toLocalDate()));
		return stationTraffic.getDeparturesInHour(dateTime.getHour());
	}
	
	public Set<StationTrafficEntryModel> getArrivalsInHour(String stationId, LocalDateTime dateTime) {
		final StationTrafficModel stationTraffic = 
				stationTrafficRepository.getById(StationTrafficModel.createIdFromNameAndDate(stationId, dateTime.toLocalDate()))
				.orElse(new StationTrafficModel(stationId, dateTime.toLocalDate()));
		return stationTraffic.getArrivalsInHour(dateTime.getHour());
	}
	
	private SearchComparator<StationModel> getStationSearchComperator() {
		return (search, station1, station2) -> {
			final boolean station1startsWithName = station1.getName().toLowerCase().startsWith(search.toLowerCase());
			final boolean station2startsWithName = station2.getName().toLowerCase().startsWith(search.toLowerCase());
			if (station1startsWithName && !station2startsWithName) return -1;
			if (!station1startsWithName && station2startsWithName) return 1;
			final int station1platforms = station1.getPlatforms() != null && !station1.getPlatforms().isEmpty() ? station1.getPlatforms().size() : 1;
			final int station2platforms = station2.getPlatforms() != null && !station2.getPlatforms().isEmpty() ? station2.getPlatforms().size() : 1;
			if (station1platforms > station2platforms) return -1;
			if (station1platforms < station2platforms) return 1;
			return 0;
		};
	}

}
