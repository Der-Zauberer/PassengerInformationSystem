package eu.derzauberer.pis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.TransportationTypeModel;
import eu.derzauberer.pis.persistence.Lazy;
import eu.derzauberer.pis.persistence.Repository;
import eu.derzauberer.pis.persistence.SearchIndex;

@Service
public class TypeService extends EntityService<TransportationTypeModel> {
	
	private final SearchIndex<TransportationTypeModel> searchComponent;
	
	@Autowired
	public TypeService(Repository<TransportationTypeModel> typeRepository) {
		super(typeRepository);
		searchComponent = new SearchIndex<>(this);
	}
	
	@Override
	public List<Lazy<TransportationTypeModel>> search(String search) {
		return searchComponent.search(search);
	}

}
