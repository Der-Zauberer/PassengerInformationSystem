package eu.derzauberer.pis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.components.SearchComponent;
import eu.derzauberer.pis.model.Type;
import eu.derzauberer.pis.repositories.EntityRepository;

@Service
public class TypeService extends EntityService<Type> {
	
	private final SearchComponent<Type> searchComponent;
	
	@Autowired
	public TypeService(EntityRepository<Type> typeRepository) {
		super(typeRepository);
		searchComponent = new SearchComponent<>(this);
		getList().forEach(searchComponent::add);
	}
	
	@Override
	public List<Type> search(String search) {
		return searchComponent.search(search);
	}

}
