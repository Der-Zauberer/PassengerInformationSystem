package eu.derzauberer.pis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.TrainOperator;
import eu.derzauberer.pis.repositories.EntityRepository;
import eu.derzauberer.pis.util.SearchTree;

@Service
public class OperatorService extends EntityService<TrainOperator> {
	
	private final EntityRepository<TrainOperator> operatorRepository;
	private final SearchTree<TrainOperator> search;
	
	@Autowired
	public OperatorService(EntityRepository<TrainOperator> operatorRepository) {
		super(operatorRepository);
		this.operatorRepository = operatorRepository;
		this.search = new SearchTree<>(operatorRepository);
		operatorRepository.getList().forEach(search::add);
	}
	
	@Override
	public void add(TrainOperator operator) {
		operatorRepository.add(operator);
		search.remove(operator);
		search.add(operator);
	}
	
	@Override
	public boolean removeById(String operatorId) {
		search.removeById(operatorId);
		return operatorRepository.removeById(operatorId);
	}
	
	public List<TrainOperator> searchByName(String name) {
		return search.searchByName(name);
	}

}
