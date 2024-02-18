package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.TransportationTypeModel;
import eu.derzauberer.pis.persistence.EntityRepository;

@Service
public class TypeService extends EntityService<TransportationTypeModel> {
	
	@Autowired
	public TypeService(EntityRepository<TransportationTypeModel> typeRepository) {
		super(typeRepository);
	}

}
