package eu.derzauberer.pis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.components.SearchComponent;
import eu.derzauberer.pis.model.TrainOperator;
import eu.derzauberer.pis.repositories.EntityRepository;

@Service
public class OperatorService extends EntityService<TrainOperator> {
	
	private final SearchComponent<TrainOperator> searchComponent;
	
	@Autowired
	public OperatorService(EntityRepository<TrainOperator> operatorRepository) {
		super(operatorRepository);
		this.searchComponent = new SearchComponent<>(this);
		getList().forEach(searchComponent::add);
	}
	
	public List<TrainOperator> search(String name) {
		return searchComponent.search(name);
	}

}
