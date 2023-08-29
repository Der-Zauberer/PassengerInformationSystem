package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.TrainType;
import eu.derzauberer.pis.repositories.EntityRepository;

@Service
public class TypeService extends EntityService<TrainType> {
	
	@Autowired
	public TypeService(EntityRepository<TrainType> typeRepository) {
		super(typeRepository);
	}

}
