package eu.derzauberer.pis.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.configuration.SpringConfiguration;
import eu.derzauberer.pis.persistence.EntityRepository;
import eu.derzauberer.pis.persistence.Lazy;
import eu.derzauberer.pis.persistence.SearchIndex;
import eu.derzauberer.pis.structure.model.LineModel;
import eu.derzauberer.pis.structure.model.LineStopModel;
import eu.derzauberer.pis.structure.model.StationModel;
import eu.derzauberer.pis.structure.model.StationTrafficModel;
import eu.derzauberer.pis.structure.model.StationTrafficEntryModel;
import eu.derzauberer.pis.util.ProgressStatus;
import eu.derzauberer.pis.util.Result;

@Service
public class LineService extends EntityService<LineModel> {
	
	private final EntityRepository<LineModel> lineRepository;
	private final EntityRepository<StationTrafficModel> stationTrafficRepository;
	private final SearchIndex<LineModel> searchComponent;
	
	@Autowired
	public LineService(EntityRepository<LineModel> lineRepository, EntityRepository<StationTrafficModel> stationTrafficRepository) throws InterruptedException {
		super(lineRepository);
		this.lineRepository = lineRepository;
		this.stationTrafficRepository = stationTrafficRepository;
		searchComponent = new SearchIndex<>(this);
		if (SpringConfiguration.indexing) {
			ProgressStatus progress = new ProgressStatus("Indexing", lineRepository.getName(), lineRepository.size());
			for (LineModel line : lineRepository.stream().map(Lazy::get).toList()) {
				addLineToTrafficIndex(line);
				progress.count();
			}
		}
	}
	
	@Override
	public LineModel save(LineModel line) {
		final LineModel savedLine = super.save(line);
		if (lineRepository.containsById(savedLine.getId())) {
			removeLineToTrafficIndex(savedLine);
		}
		addLineToTrafficIndex(savedLine);
		return savedLine;
	}
	
	@Override
	public boolean removeById(String lineId) {
		lineRepository.getById(lineId).ifPresent(this::removeLineToTrafficIndex);
		return lineRepository.removeById(lineId);
	}
	
	@Override
	public Result<LineModel> search(String search) {
		return searchComponent.search(search);
	}
	
	public SortedSet<StationTrafficEntryModel> findArrivalsSinceHour(StationModel station, LocalDateTime dateTime, int limit) {
		final StationTrafficModel traffic = getOrCreateStationTraffic(station.getId(), dateTime.toLocalDate());
		final SortedSet<StationTrafficEntryModel> results = new TreeSet<>();
		int i = 0;
		for (int j = dateTime.getHour(); j < 23; j++) {
			for (StationTrafficEntryModel entry : traffic.getArrivalsInHour(j)) {
				results.add(entry);
				if (i++ >= limit) {
					return results;
				}
			}
		}
		return results;
	}
	
	public int getAmountOfArrivalsSinceHour(StationModel station, LocalDateTime dateTime, int limit) {
		final StationTrafficModel traffic = getOrCreateStationTraffic(station.getId(), dateTime.toLocalDate());
		int i = 0;
		for (int j = dateTime.getHour(); j < 23; j++) {
			i += traffic.getArrivalsInHour(j).size();
		}
		return i;
	}
	
	public SortedSet<StationTrafficEntryModel> findDeparturesSinceHour(StationModel station, LocalDateTime dateTime, int limit) {
		final StationTrafficModel traffic = getOrCreateStationTraffic(station.getId(), dateTime.toLocalDate());
		final SortedSet<StationTrafficEntryModel> results = new TreeSet<>();
		int i = 0;
		for (int j = dateTime.getHour(); j < 23; j++) {
			for (StationTrafficEntryModel entry : traffic.getDeparturesInHour(j)) {
				results.add(entry);
				if (i++ >= limit) {
					return results;
				}
			}
		}
		return results;
	}
	
	public int getAmountOfDeparturesSinceHour(StationModel station, LocalDateTime dateTime, int limit) {
		final StationTrafficModel traffic = getOrCreateStationTraffic(station.getId(), dateTime.toLocalDate());
		int i = 0;
		for (int j = dateTime.getHour(); j < 23; j++) {
			i += traffic.getDeparturesInHour(j).size();
		}
		return i;
	}
	
	public String createLineId() {
		return String.format("%08x", Long.valueOf(System.nanoTime()).toString().hashCode());
	}
	
	private void addLineToTrafficIndex(LineModel line) {
		int i = 0;
		for (LineStopModel stop : line.getStops()) {
			final StationTrafficModel arrivalStationTraffic = getOrCreateStationTraffic(stop.getStationId(), stop.getDeparture().toLocalDate());
			arrivalStationTraffic.addArrival(new StationTrafficEntryModel(stop.getArrival().toLocalTime(), line.getId(), i, stop.getPlatform(), stop.getPlatfromArea(), line.getLastStop().equals(stop) ? true : false));
			stationTrafficRepository.save(arrivalStationTraffic);
			final StationTrafficModel departureStationTraffic = getOrCreateStationTraffic(stop.getStationId(), stop.getArrival().toLocalDate());
			departureStationTraffic.addDeparture(new StationTrafficEntryModel(stop.getDeparture().toLocalTime(), line.getId(), i++, stop.getPlatform(), stop.getPlatfromArea(), line.getLastStop().equals(stop) ? true : false));
			stationTrafficRepository.save(departureStationTraffic);
		}
	}
	
	private void removeLineToTrafficIndex(LineModel line) {
		for (LineStopModel stop : line.getStops()) {
			final StationTrafficModel arrivalStationTraffic = getOrCreateStationTraffic(stop.getStationId(), stop.getDeparture().toLocalDate());
			arrivalStationTraffic.removeArrival(stop.getArrival().toLocalTime(), line.getId());
			stationTrafficRepository.save(arrivalStationTraffic);
			final StationTrafficModel departureStationTraffic = getOrCreateStationTraffic(stop.getStationId(), stop.getArrival().toLocalDate());
			departureStationTraffic.removeDeparture(stop.getDeparture().toLocalTime(), line.getId());
			stationTrafficRepository.save(departureStationTraffic);
		}
	}
	
	private StationTrafficModel getOrCreateStationTraffic(String stationId, LocalDate date) {
		return stationTrafficRepository
				.getById(StationTrafficModel.createIdFromNameAndDate(stationId, date))
				.orElse(new StationTrafficModel(stationId, date));
	}

}
