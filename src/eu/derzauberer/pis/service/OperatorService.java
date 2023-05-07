package eu.derzauberer.pis.service;

import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.TrainOperator;
import eu.derzauberer.pis.repositories.MemoryRepository;

@Service
public class OperatorService extends EntityService<TrainOperator> {

	public OperatorService() {
		super("operators", new MemoryRepository<>("operators", TrainOperator.class));
	}

}
