package eu.derzauberer.pis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.TrainType;
import eu.derzauberer.pis.repositories.Repository;

@Service
public class TypeService {
	
	private final Repository<TrainType> typeRepository;
	
	@Autowired
	public TypeService(Repository<TrainType> typeRepository) {
		this.typeRepository = typeRepository;
	}
	
	public void add(TrainType type) {
		typeRepository.add(type);
	}
	
	public boolean removeById(String typeId) {
		return typeRepository.removeById(typeId);
	}
	
	public boolean containsById(String typeId) {
		return typeRepository.containsById(typeId);
	}
	
	public Optional<TrainType> getById(String typeId) {
		return typeRepository.getById(typeId);
	}
	
	public List<TrainType> getTypes() {
		return typeRepository.getList();
	}
	
	public int size() {
		return typeRepository.size();
	}

}
