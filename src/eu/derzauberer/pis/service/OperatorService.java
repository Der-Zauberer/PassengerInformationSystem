package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.TrainOperator;
import eu.derzauberer.pis.repositories.EntityRepository;

@Service
public class OperatorService extends EntityService<TrainOperator> {
	
	@Autowired
	public OperatorService(EntityRepository<TrainOperator> operatorRepository) {
		super(operatorRepository);
	}

}
