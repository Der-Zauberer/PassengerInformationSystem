package eu.derzauberer.pis.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class StationTraffic {
	
	private final LocalDate date;
	private final Map<Integer, TreeSet<TrainTrafficEntry>> departures = new HashMap<>();
	private final Map<Integer, TreeSet<TrainTrafficEntry>> arrivals = new HashMap<>();
	
	public StationTraffic(LocalDate date) {
		super();
		this.date = date;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void addDeparture(TrainTrafficEntry entry) {
		departures.get(entry.getTime().getHour()).add(entry);
	}
	
	public void removeDeparture(TrainTrafficEntry entry) {
		departures.get(entry.getTime().getHour()).remove(entry);
	}
	
	public TreeSet<TrainTrafficEntry> getDeparturesInHour(int hour) {
		return new TreeSet<>(departures.get(hour));
		
	}
	
	public TreeSet<TrainTrafficEntry> getDeparturesSinceHour(int hour) {
		final TreeSet<TrainTrafficEntry> departureSet = new TreeSet<>();
		for (int i = hour; i < 24; i++) {
			departureSet.addAll(arrivals.get(hour));
		}
		return departureSet;
	}
	
	public void addArrival(TrainTrafficEntry entry) {
		arrivals.get(entry.getTime().getHour()).add(entry);
	}
	
	public void removeArrival(TrainTrafficEntry entry) {
		arrivals.get(entry.getTime().getHour()).remove(entry);
	}
	
	public TreeSet<TrainTrafficEntry> getArrivalsInHour(int hour) {
		return new TreeSet<>(arrivals.get(hour));
	}
	
	public TreeSet<TrainTrafficEntry> getArrivalsSinceHour(int hour) {
		final TreeSet<TrainTrafficEntry> arrivalSet = new TreeSet<>();
		for (int i = hour; i <= 24; i++) {
			arrivalSet.addAll(arrivals.get(hour));
		}
		return arrivalSet;
	}

}
