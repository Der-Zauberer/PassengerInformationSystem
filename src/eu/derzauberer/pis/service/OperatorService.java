package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.components.SearchComponent;
import eu.derzauberer.pis.repository.EntityRepository;
import eu.derzauberer.pis.structure.model.OperatorModel;
import eu.derzauberer.pis.util.Result;

@Service
public class OperatorService extends EntityService<OperatorModel> {
	
	private final SearchComponent<OperatorModel> searchComponent;
	
	@Autowired
	public OperatorService(EntityRepository<OperatorModel> operatorRepository) {
		super(operatorRepository);
		this.searchComponent = new SearchComponent<>(this);
	}
	
	public Result<OperatorModel> search(String name) {
		return searchComponent.search(name);
	}

}
