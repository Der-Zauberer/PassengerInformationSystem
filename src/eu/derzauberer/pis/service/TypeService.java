package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.persistence.Repository;
import eu.derzauberer.pis.persistence.SearchIndex;
import eu.derzauberer.pis.structure.model.TransportationTypeModel;
import eu.derzauberer.pis.util.Result;

@Service
public class TypeService extends EntityService<TransportationTypeModel> {
	
	private final SearchIndex<TransportationTypeModel> searchComponent;
	
	@Autowired
	public TypeService(Repository<TransportationTypeModel> typeRepository) {
		super(typeRepository);
		searchComponent = new SearchIndex<>(this);
	}
	
	@Override
	public Result<TransportationTypeModel> search(String search) {
		return searchComponent.search(search);
	}

}
