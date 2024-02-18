package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.OperatorModel;
import eu.derzauberer.pis.persistence.EntityRepository;

@Service
public class OperatorService extends EntityService<OperatorModel> {
	
	@Autowired
	public OperatorService(EntityRepository<OperatorModel> operatorRepository) {
		super(operatorRepository);
	}

}
