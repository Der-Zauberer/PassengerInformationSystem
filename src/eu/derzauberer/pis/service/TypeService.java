package eu.derzauberer.pis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.components.SearchComponent;
import eu.derzauberer.pis.model.TrainType;
import eu.derzauberer.pis.repositories.EntityRepository;

@Service
public class TypeService extends EntityService<TrainType> {
	
	private final SearchComponent<TrainType> searchComponent;
	
	@Autowired
	public TypeService(EntityRepository<TrainType> typeRepository) {
		super(typeRepository);
		searchComponent = new SearchComponent<>(this);
		getList().forEach(searchComponent::add);
	}
	
	@Override
	public List<TrainType> search(String search) {
		return searchComponent.search(search);
	}

}
