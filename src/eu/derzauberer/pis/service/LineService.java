package eu.derzauberer.pis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.LineSceduled;
import eu.derzauberer.pis.repositories.FileRepository;
import eu.derzauberer.pis.repositories.Repository;

@Service
public class LineService {
	
	final Repository<LineSceduled> lineRepository = new FileRepository<>("lines", LineSceduled.class);
	
	public void add(LineSceduled line) {
		lineRepository.add(line);
	}
	
	public boolean removeById(String lineId) {
		return lineRepository.removeById(lineId);
	}
	
	public boolean containsById(String lineId) {
		return lineRepository.containsById(lineId);
	}
	
	public Optional<LineSceduled> getById(String lineId) {
		return lineRepository.getById(lineId);
	}
	
	public List<LineSceduled> getLines() {
		return lineRepository.getList();
	}
	
	public int size() {
		return lineRepository.size();
	}

}
