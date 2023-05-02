package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Line<T extends LineStop> {
	
	private final String id;
	private TrainType type;
	private int number;
	private String operatorId;
	private List<T> stops;
	private ApiInformation api;
	
	@ConstructorProperties({ "id", "type", "number" })
	public Line(String id, TrainType type, int number) {
		this.id = id;
		this.type = type;
		this.number = number;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return type.getId() + number;
	}
	
	public TrainType getType() {
		return type;
	}

	public void setType(TrainType type) {
		this.type = type;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	
	public void addStop(T stop) {
		if (stops == null) stops = new ArrayList<>();
		stops.add(stop);
	}
	
	public void addStop(int position, T stop) {
		if (stops == null) { 
			final List<T> newStops = new ArrayList<>();
			newStops.set(position, stop);
			stops = newStops;
		} else {
			stops.set(position, stop);
		}
	}
	
	public void removeStop(T stop) {
		if (stops.isEmpty()) return;
		stops.remove(stop);
		if (stops.isEmpty()) stop = null;
	}
	
	public void removeStop(String stationId) {
		if (stops.isEmpty()) return;
		stops.remove(getStop(stationId));
		if (stops.isEmpty()) stops = null;
	}
	
	public void removeStop(int position) {
		if (stops.isEmpty()) throw new IndexOutOfBoundsException("Index: " + position + ", Size: " + 0);
		stops.remove(position);
		if (stops.isEmpty()) stops = null;
	}
	
	public T getStop(String stationId) {
		return stops.stream().filter(stop -> stop.getStationId().equals(stationId)).findAny().orElse(null);
	}
	
	public T getStop(int position) {
		return stops.get(position);
	}
	
	public int getAmountOfStops() {
		return stops != null ? stops.size() : 0;
	}
	
	public T getFirstStop() {
		return stops != null && !stops.isEmpty() ? stops.get(0) : null;
	}
	
	public T getLastStop() {
		return stops != null && !stops.isEmpty() ? stops.get(stops.size() - 1) : null;
	}
	
	public List<T> getStops() {
		return Collections.unmodifiableList(stops != null ? stops : new ArrayList<>());
	}
	
	public ApiInformation getApiInformation() {
		return api;
	}
	
	public ApiInformation getOrCreateApiInformtion() {
		if (api == null) api = new ApiInformation();
		return api;
	}
	
	public void setApiInformation(ApiInformation api) {
		this.api = api;
	}

}
