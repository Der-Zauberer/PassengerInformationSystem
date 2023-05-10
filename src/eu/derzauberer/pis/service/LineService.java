package eu.derzauberer.pis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.LineLiveData;
import eu.derzauberer.pis.model.LineStop;
import eu.derzauberer.pis.model.StationTraffic;
import eu.derzauberer.pis.model.StationTrafficEntry;
import eu.derzauberer.pis.repositories.Repository;

@Service
public class LineService {
	
	private final Repository<LineLiveData> lineRepository;
	private final Repository<StationTraffic> stationTrafficRepository;
	
	@Autowired
	public LineService(Repository<LineLiveData> lineRepository, Repository<StationTraffic> stationTrafficRepository) {
		this.lineRepository = lineRepository;
		this.stationTrafficRepository = stationTrafficRepository;
	}
	
	public void add(LineLiveData line) {
		if (lineRepository.containsById(line.getId())) {
			removeLineToTrafficIndex(line);
		}
		lineRepository.add(line);
		addLineToTrafficIndex(line);
	}
	
	public boolean removeById(String lineId) {
		lineRepository.getById(lineId).ifPresent(this::removeLineToTrafficIndex);
		return lineRepository.removeById(lineId);
	}
	
	public boolean containsById(String lineId) {
		return lineRepository.containsById(lineId);
	}
	
	public Optional<LineLiveData> getById(String lineId) {
		return lineRepository.getById(lineId);
	}
	
	public List<LineLiveData> getLines() {
		return lineRepository.getList();
	}
	
	public int size() {
		return lineRepository.size();
	}
	
	private void addLineToTrafficIndex(LineLiveData line) {
		int i = 0;
		for (LineStop stop : line.getStops()) {
			final String stationTrafficId = StationTraffic.createIdFormNameAndDate(stop.getStationId(), line.getDate());
			final StationTraffic stationTraffic = stationTrafficRepository.getById(stationTrafficId).orElse(new StationTraffic(stop.getStationId(), line.getDate()));
			stationTraffic.addArrival(new StationTrafficEntry(stop.getArrival(), line.getId(), i, line.getLastStop().equals(stop) ? true : false));
			stationTraffic.addDeparture(new StationTrafficEntry(stop.getDeparture(), line.getId(), i++, line.getLastStop().equals(stop) ? true : false));
			stationTrafficRepository.add(stationTraffic);
		}
	}
	
	private void removeLineToTrafficIndex(LineLiveData line) {
		for (LineStop stop : line.getStops()) {
			final String stationTrafficId = StationTraffic.createIdFormNameAndDate(stop.getStationId(), line.getDate());
			final StationTraffic stationTraffic = stationTrafficRepository.getById(stationTrafficId).orElse(new StationTraffic(stop.getStationId(), line.getDate()));
			stationTraffic.removeArrival(stop.getArrival(), line.getId());
			stationTraffic.removeArrival(stop.getDeparture(), line.getId());
			stationTrafficRepository.add(stationTraffic);
		}
	}

}
