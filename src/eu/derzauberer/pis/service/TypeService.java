package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.components.SearchComponent;
import eu.derzauberer.pis.repository.EntityRepository;
import eu.derzauberer.pis.structure.model.TransportationTypeModel;
import eu.derzauberer.pis.util.Result;

@Service
public class TypeService extends EntityService<TransportationTypeModel> {
	
	private final SearchComponent<TransportationTypeModel> searchComponent;
	
	@Autowired
	public TypeService(EntityRepository<TransportationTypeModel> typeRepository) {
		super(typeRepository);
		searchComponent = new SearchComponent<>(this);
	}
	
	@Override
	public Result<TransportationTypeModel> search(String search) {
		return searchComponent.search(search);
	}

}
