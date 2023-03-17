package eu.derzauberer.pis.service;

import eu.derzauberer.pis.model.TrainOperator;
import eu.derzauberer.pis.util.MemoryRepository;
import eu.derzauberer.pis.util.Service;

public class OperatorService extends Service<TrainOperator> {

	public OperatorService() {
		super("operators", new MemoryRepository<>("operators", TrainOperator.class));
	}

}
