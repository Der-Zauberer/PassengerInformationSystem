package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.TrainOperator;
import eu.derzauberer.pis.repositories.Repository;

@Service
public class OperatorService extends EntityService<TrainOperator> {
	
	@Autowired
	public OperatorService(Repository<TrainOperator> operatorRepository) {
		super(operatorRepository);
	}

}
