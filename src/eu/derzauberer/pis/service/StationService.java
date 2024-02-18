package eu.derzauberer.pis.service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.SortedSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.StationModel;
import eu.derzauberer.pis.model.StationTrafficEntryModel;
import eu.derzauberer.pis.model.StationTrafficModel;
import eu.derzauberer.pis.persistence.EntityRepository;
import eu.derzauberer.pis.persistence.Repository;

@Service
public class StationService extends EntityService<StationModel> {
	
	private final Repository<StationTrafficModel> stationTrafficRepository;
	
	@Autowired
	public StationService(EntityRepository<StationModel> stationRepository, Repository<StationTrafficModel> stationTrafficRepository) {
		super(stationRepository);
		this.stationTrafficRepository = stationTrafficRepository;
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

}
