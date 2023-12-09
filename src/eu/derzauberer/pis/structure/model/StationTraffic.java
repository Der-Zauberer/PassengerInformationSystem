package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import eu.derzauberer.pis.structrue.container.StationTrafficEntry;

public class StationTraffic extends Entity<StationTraffic> {
	
	private final String id;
	private final Map<Integer, SortedSet<StationTrafficEntry>> departures = new HashMap<>();
	private final Map<Integer, SortedSet<StationTrafficEntry>> arrivals = new HashMap<>();
	
	@ConstructorProperties({ "id" })
	private StationTraffic(String id) {
		this.id = id;
	}
	
	public StationTraffic(String stationId, LocalDate date) {
		this.id = createIdFromNameAndDate(stationId, date);
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	public void addDeparture(StationTrafficEntry entry) {
		final int hour = entry.getTime().getHour();
		if (departures.get(hour) == null) departures.put(hour, new TreeSet<>());
		departures.get(hour).add(entry);
	}
	
	public void removeDeparture(LocalTime time, String lineId) {
		if (departures.get(time.getHour()) == null) return; 
		departures.get(time.getHour()).stream()
			.filter(entry -> entry.getLineId().equals(lineId))
			.findAny()
			.ifPresent(departures.get(time.getHour())::remove);
		if (departures.get(time.getHour()).isEmpty()) departures.remove(time.getHour());
	}
	
	public SortedSet<StationTrafficEntry> getDeparturesInHour(int hour) {
		final SortedSet<StationTrafficEntry> departureSet = departures.get(hour);
		return Collections.unmodifiableSortedSet(departureSet != null ? departureSet : new TreeSet<>());
	}
	
	public void addArrival(StationTrafficEntry entry) {
		final int hour = entry.getTime().getHour();
		if (arrivals.get(hour) == null) arrivals.put(hour, new TreeSet<>());
		arrivals.get(hour).add(entry);
	}
	
	public void removeArrival(LocalTime time, String lineId) {
		if (arrivals.get(time.getHour()) == null) return; 
		arrivals.get(time.getHour()).stream()
				.filter(entry -> entry.getLineId().equals(lineId))
				.findAny()
				.ifPresent(arrivals.get(time.getHour())::remove);
		if (arrivals.get(time.getHour()).isEmpty()) arrivals.remove(time.getHour());
	}
	
	public SortedSet<StationTrafficEntry> getArrivalsInHour(int hour) {
		final SortedSet<StationTrafficEntry> arrivalSet = arrivals.get(hour);
		return Collections.unmodifiableSortedSet(arrivalSet != null ? arrivalSet : new TreeSet<>());
	}
	
	public static String createIdFromNameAndDate(String stationId, LocalDate date) {
		return String.format("%1$s_%2$04d%3$02d%4$02d", stationId, date.getYear(), date.getMonthValue(), date.getDayOfMonth());
	}
	
	@Override
	public StationTraffic copy() {
		final StationTraffic stationTraffic = new StationTraffic(this.id);
		this.departures.forEach((hour, traffic) -> {
			stationTraffic.departures.put(hour, traffic
					.stream()
					.map(StationTrafficEntry::new)
					.collect(Collectors.toCollection(TreeSet::new)));
		});
		this.arrivals.forEach((hour, traffic) -> {
			stationTraffic.arrivals.put(hour, traffic
					.stream()
					.map(StationTrafficEntry::new)
					.collect(Collectors.toCollection(TreeSet::new)));
		});
		return stationTraffic;
	}

}
