package eu.derzauberer.pis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.components.SearchComponent;
import eu.derzauberer.pis.model.Operator;
import eu.derzauberer.pis.repositories.EntityRepository;

@Service
public class OperatorService extends EntityService<Operator> {
	
	private final SearchComponent<Operator> searchComponent;
	
	@Autowired
	public OperatorService(EntityRepository<Operator> operatorRepository) {
		super(operatorRepository);
		this.searchComponent = new SearchComponent<>(this);
	}
	
	public List<Operator> search(String name) {
		return searchComponent.search(name);
	}

}
