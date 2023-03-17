package eu.derzauberer.pis.service;

import eu.derzauberer.pis.model.TrainType;
import eu.derzauberer.pis.util.MemoryRepository;
import eu.derzauberer.pis.util.Service;

public class TypeService extends Service<TrainType> {

	public TypeService() {
		super("types", new MemoryRepository<>("types", TrainType.class));
	}

}
