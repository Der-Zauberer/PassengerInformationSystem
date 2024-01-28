package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.persistence.Repository;
import eu.derzauberer.pis.persistence.SearchIndex;
import eu.derzauberer.pis.structure.model.OperatorModel;
import eu.derzauberer.pis.util.Result;

@Service
public class OperatorService extends EntityService<OperatorModel> {
	
	private final SearchIndex<OperatorModel> searchComponent;
	
	@Autowired
	public OperatorService(Repository<OperatorModel> operatorRepository) {
		super(operatorRepository);
		this.searchComponent = new SearchIndex<>(this);
	}
	
	public Result<OperatorModel> search(String name) {
		return searchComponent.search(name);
	}

}
