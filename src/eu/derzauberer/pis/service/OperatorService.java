package eu.derzauberer.pis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.TrainOperator;
import eu.derzauberer.pis.repositories.MemoryRepository;
import eu.derzauberer.pis.repositories.Repository;

@Service
public class OperatorService {
	
	final Repository<TrainOperator> operatorRepository = new MemoryRepository<>("operators", TrainOperator.class);
	
	public void add(TrainOperator operator) {
		operatorRepository.add(operator);
	}
	
	public boolean removeById(String operatorId) {
		return operatorRepository.removeById(operatorId);
	}
	
	public boolean containsById(String operatorId) {
		return operatorRepository.containsById(operatorId);
	}
	
	public Optional<TrainOperator> getById(String operatorId) {
		return operatorRepository.getById(operatorId);
	}
	
	public List<TrainOperator> getOperators() {
		return operatorRepository.getList();
	}
	
	public int size() {
		return operatorRepository.size();
	}

}
