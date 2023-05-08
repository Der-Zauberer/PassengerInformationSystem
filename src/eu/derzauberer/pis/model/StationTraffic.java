package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class StationTraffic implements Entity<StationTraffic> {
	
	private final String stationId;
	private final LocalDate date;
	private final Map<Integer, TreeSet<StationTrafficEntry>> departures = new HashMap<>();
	private final Map<Integer, TreeSet<StationTrafficEntry>> arrivals = new HashMap<>();
	
	@ConstructorProperties({ "stationId", "date" })
	public StationTraffic(String stationId, LocalDate date) {
		super();
		this.stationId = stationId;
		this.date = date;
	}
	
	@Override
	public String getId() {
		return stationId;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void addDeparture(StationTrafficEntry entry) {
		departures.get(entry.getTime().getHour()).add(entry);
	}
	
	public void removeDeparture(StationTrafficEntry entry) {
		departures.get(entry.getTime().getHour()).remove(entry);
	}
	
	public TreeSet<StationTrafficEntry> getDeparturesInHour(int hour) {
		return new TreeSet<>(departures.get(hour));
	}
	
	public TreeSet<StationTrafficEntry> getDeparturesSinceHour(int hour) {
		final TreeSet<StationTrafficEntry> departureSet = new TreeSet<>();
		for (int i = hour; i < 24; i++) {
			departureSet.addAll(arrivals.get(hour));
		}
		return departureSet;
	}
	
	public void addArrival(StationTrafficEntry entry) {
		arrivals.get(entry.getTime().getHour()).add(entry);
	}
	
	public void removeArrival(StationTrafficEntry entry) {
		arrivals.get(entry.getTime().getHour()).remove(entry);
	}
	
	public TreeSet<StationTrafficEntry> getArrivalsInHour(int hour) {
		return new TreeSet<>(arrivals.get(hour));
	}
	
	public TreeSet<StationTrafficEntry> getArrivalsSinceHour(int hour) {
		final TreeSet<StationTrafficEntry> arrivalSet = new TreeSet<>();
		for (int i = hour; i <= 24; i++) {
			arrivalSet.addAll(arrivals.get(hour));
		}
		return arrivalSet;
	}

}
