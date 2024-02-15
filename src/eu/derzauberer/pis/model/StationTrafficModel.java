package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class StationTrafficModel extends EntityModel<StationTrafficModel> {
	
	private final String id;
	private final Map<Integer, SortedSet<StationTrafficEntryModel>> departures = new HashMap<>();
	private final Map<Integer, SortedSet<StationTrafficEntryModel>> arrivals = new HashMap<>();
	
	@ConstructorProperties({ "id" })
	private StationTrafficModel(String id) {
		this.id = id;
	}
	
	public StationTrafficModel(String stationId, LocalDate date) {
		this.id = createIdFromNameAndDate(stationId, date);
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	public void addDeparture(StationTrafficEntryModel entry) {
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
	
	public SortedSet<StationTrafficEntryModel> getDeparturesInHour(int hour) {
		final SortedSet<StationTrafficEntryModel> departureSet = departures.get(hour);
		return Collections.unmodifiableSortedSet(departureSet != null ? departureSet : new TreeSet<>());
	}
	
	public void addArrival(StationTrafficEntryModel entry) {
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
	
	public SortedSet<StationTrafficEntryModel> getArrivalsInHour(int hour) {
		final SortedSet<StationTrafficEntryModel> arrivalSet = arrivals.get(hour);
		return Collections.unmodifiableSortedSet(arrivalSet != null ? arrivalSet : new TreeSet<>());
	}
	
	public static String createIdFromNameAndDate(String stationId, LocalDate date) {
		return String.format("%1$s_%2$04d%3$02d%4$02d", stationId, date.getYear(), date.getMonthValue(), date.getDayOfMonth());
	}
	
	@Override
	public StationTrafficModel copy() {
		final StationTrafficModel stationTraffic = new StationTrafficModel(this.id);
		this.departures.forEach((hour, traffic) -> {
			stationTraffic.departures.put(hour, traffic
					.stream()
					.map(StationTrafficEntryModel::new)
					.collect(Collectors.toCollection(TreeSet::new)));
		});
		this.arrivals.forEach((hour, traffic) -> {
			stationTraffic.arrivals.put(hour, traffic
					.stream()
					.map(StationTrafficEntryModel::new)
					.collect(Collectors.toCollection(TreeSet::new)));
		});
		return stationTraffic;
	}

}
