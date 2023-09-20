package eu.derzauberer.pis.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.components.SearchComponent;
import eu.derzauberer.pis.configuration.SpringConfiguration;
import eu.derzauberer.pis.model.Line;
import eu.derzauberer.pis.model.LineStop;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.model.StationTraffic;
import eu.derzauberer.pis.model.StationTrafficEntry;
import eu.derzauberer.pis.repositories.EntityRepository;
import eu.derzauberer.pis.util.ProgressStatus;

@Service
public class LineService extends EntityService<Line> {
	
	private final EntityRepository<Line> lineRepository;
	private final EntityRepository<StationTraffic> stationTrafficRepository;
	private final SearchComponent<Line> searchComponent;
	
	@Autowired
	public LineService(EntityRepository<Line> lineRepository, EntityRepository<StationTraffic> stationTrafficRepository) throws InterruptedException {
		super(lineRepository);
		this.lineRepository = lineRepository;
		this.stationTrafficRepository = stationTrafficRepository;
		searchComponent = new SearchComponent<>(this);
		if (SpringConfiguration.indexing) {
			ProgressStatus progress = new ProgressStatus("Indexing", lineRepository.getName(), lineRepository.size());
			for (Line line : lineRepository.getList()) {
				addLineToTrafficIndex(line);
				progress.count();
			}
		}
	}
	
	@Override
	public void add(Line line) {
		if (lineRepository.containsById(line.getId())) {
			removeLineToTrafficIndex(line);
		}
		lineRepository.add(line);
		addLineToTrafficIndex(line);
	}
	
	@Override
	public boolean removeById(String lineId) {
		lineRepository.getById(lineId).ifPresent(this::removeLineToTrafficIndex);
		return lineRepository.removeById(lineId);
	}
	
	@Override
	public List<Line> search(String search) {
		return searchComponent.search(search);
	}
	
	public SortedSet<StationTrafficEntry> findArrivalsSinceHour(Station station, LocalDateTime dateTime, int limit) {
		final StationTraffic traffic = getOrCreateStationTraffic(station.getId(), dateTime.toLocalDate());
		final SortedSet<StationTrafficEntry> results = new TreeSet<>();
		int i = 0;
		for (int j = dateTime.getHour(); j < 23; j++) {
			for (StationTrafficEntry entry : traffic.getArrivalsInHour(j)) {
				results.add(entry);
				if (i++ >= limit) {
					return results;
				}
			}
		}
		return results;
	}
	
	public int getAmountOfArrivalsSinceHour(Station station, LocalDateTime dateTime, int limit) {
		final StationTraffic traffic = getOrCreateStationTraffic(station.getId(), dateTime.toLocalDate());
		int i = 0;
		for (int j = dateTime.getHour(); j < 23; j++) {
			i += traffic.getArrivalsInHour(j).size();
		}
		return i;
	}
	
	public SortedSet<StationTrafficEntry> findDeparturesSinceHour(Station station, LocalDateTime dateTime, int limit) {
		final StationTraffic traffic = getOrCreateStationTraffic(station.getId(), dateTime.toLocalDate());
		final SortedSet<StationTrafficEntry> results = new TreeSet<>();
		int i = 0;
		for (int j = dateTime.getHour(); j < 23; j++) {
			for (StationTrafficEntry entry : traffic.getDeparturesInHour(j)) {
				results.add(entry);
				if (i++ >= limit) {
					return results;
				}
			}
		}
		return results;
	}
	
	public int getAmountOfDeparturesSinceHour(Station station, LocalDateTime dateTime, int limit) {
		final StationTraffic traffic = getOrCreateStationTraffic(station.getId(), dateTime.toLocalDate());
		int i = 0;
		for (int j = dateTime.getHour(); j < 23; j++) {
			i += traffic.getDeparturesInHour(j).size();
		}
		return i;
	}
	
	public String createLineId() {
		return String.format("%08x", Long.valueOf(System.nanoTime()).toString().hashCode());
	}
	
	private void addLineToTrafficIndex(Line line) {
		int i = 0;
		for (LineStop stop : line.getStops()) {
			final StationTraffic arrivalStationTraffic = getOrCreateStationTraffic(stop.getStationId(), stop.getDeparture().toLocalDate());
			arrivalStationTraffic.addArrival(new StationTrafficEntry(stop.getArrival().toLocalTime(), line.getId(), i, stop.getPlatform(), stop.getPlatfromArea(), line.getLastStop().equals(stop) ? true : false));
			stationTrafficRepository.add(arrivalStationTraffic);
			final StationTraffic departureStationTraffic = getOrCreateStationTraffic(stop.getStationId(), stop.getArrival().toLocalDate());
			departureStationTraffic.addDeparture(new StationTrafficEntry(stop.getDeparture().toLocalTime(), line.getId(), i++, stop.getPlatform(), stop.getPlatfromArea(), line.getLastStop().equals(stop) ? true : false));
			stationTrafficRepository.add(departureStationTraffic);
		}
	}
	
	private void removeLineToTrafficIndex(Line line) {
		for (LineStop stop : line.getStops()) {
			final StationTraffic arrivalStationTraffic = getOrCreateStationTraffic(stop.getStationId(), stop.getDeparture().toLocalDate());
			arrivalStationTraffic.removeArrival(stop.getArrival().toLocalTime(), line.getId());
			stationTrafficRepository.add(arrivalStationTraffic);
			final StationTraffic departureStationTraffic = getOrCreateStationTraffic(stop.getStationId(), stop.getArrival().toLocalDate());
			departureStationTraffic.removeDeparture(stop.getDeparture().toLocalTime(), line.getId());
			stationTrafficRepository.add(departureStationTraffic);
		}
	}
	
	private StationTraffic getOrCreateStationTraffic(String stationId, LocalDate date) {
		return stationTrafficRepository
				.getById(StationTraffic.createIdFormNameAndDate(stationId, date))
				.orElse(new StationTraffic(stationId, date));
	}

}
