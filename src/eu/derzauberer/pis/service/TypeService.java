package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.TrainType;
import eu.derzauberer.pis.repositories.Repository;

@Service
public class TypeService extends EntityService<TrainType> {
	
	
	@Autowired
	public TypeService(Repository<TrainType> typeRepository) {
		super(typeRepository);
	}

}
