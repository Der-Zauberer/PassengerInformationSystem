package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.components.SearchComponent;
import eu.derzauberer.pis.model.Operator;
import eu.derzauberer.pis.repository.EntityRepository;
import eu.derzauberer.pis.util.Result;

@Service
public class OperatorService extends EntityService<Operator> {
	
	private final SearchComponent<Operator> searchComponent;
	
	@Autowired
	public OperatorService(EntityRepository<Operator> operatorRepository) {
		super(operatorRepository);
		this.searchComponent = new SearchComponent<>(this);
	}
	
	public Result<Operator> search(String name) {
		return searchComponent.search(name);
	}

}
