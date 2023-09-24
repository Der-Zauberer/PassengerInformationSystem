package eu.derzauberer.pis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.components.SearchComponent;
import eu.derzauberer.pis.model.TransportationType;
import eu.derzauberer.pis.repositories.EntityRepository;

@Service
public class TypeService extends EntityService<TransportationType> {
	
	private final SearchComponent<TransportationType> searchComponent;
	
	@Autowired
	public TypeService(EntityRepository<TransportationType> typeRepository) {
		super(typeRepository);
		searchComponent = new SearchComponent<>(this);
	}
	
	@Override
	public List<TransportationType> search(String search) {
		return searchComponent.search(search);
	}

}
