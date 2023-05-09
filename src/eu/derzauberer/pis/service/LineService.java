package eu.derzauberer.pis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.LineSceduled;
import eu.derzauberer.pis.repositories.Repository;

@Service
public class LineService {
	
	private final Repository<LineSceduled> lineRepository;
	
	@Autowired
	public LineService(Repository<LineSceduled> lineRepository) {
		this.lineRepository = lineRepository;
	}
	
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
