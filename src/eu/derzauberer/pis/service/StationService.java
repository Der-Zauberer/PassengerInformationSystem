package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.StationModel;
import eu.derzauberer.pis.persistence.EntityRepository;

@Service
public class StationService extends EntityService<StationModel> {
	
	@Autowired
	public StationService(EntityRepository<StationModel> stationRepository) {
		super(stationRepository);
	}

}
