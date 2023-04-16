package eu.derzauberer.pis.service;

import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.TrainType;
import eu.derzauberer.pis.util.EntityService;
import eu.derzauberer.pis.util.MemoryRepository;

@Service
public class TypeService extends EntityService<TrainType> {

	public TypeService() {
		super("types", new MemoryRepository<>("types", TrainType.class));
	}

}
