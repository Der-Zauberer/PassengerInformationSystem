package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import eu.derzauberer.pis.structrue.container.ApiInformation;
import eu.derzauberer.pis.structrue.container.Information;
import eu.derzauberer.pis.structrue.container.LineScedule;
import eu.derzauberer.pis.structrue.container.RouteStop;

public class Route extends Entity<Route> implements NameEntity {
	
	private final String id;
	private String name;
	private TransportationType type;
	private int number;
	private String operatorId;
	private List<RouteStop> stops;
	private LineScedule scedule;
	private List<Information> informations;
	private ApiInformation api;
	
	@ConstructorProperties({ "id", "name", "type" , "number"})
	public Route(String id, String name, TransportationType type, int number) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.number = number;
		this.scedule = new LineScedule();
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public TransportationType getType() {
		return type;
	}
	
	public void setType(TransportationType type) {
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
	
	public void addStop(RouteStop stop) {
		if (stops == null) stops = new ArrayList<>();
		stops.add(stop);
	}
	
	public void addStop(int position, RouteStop stop) {
		if (stops == null) { 
			final List<RouteStop> newStops = new ArrayList<>();
			newStops.set(position, stop);
			stops = newStops;
		} else {
			stops.set(position, stop);
		}
	}
	
	public void removeStop(RouteStop stop) {
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
	
	public RouteStop getStop(String stationId) {
		if (stops == null) return null;
		return stops.stream().filter(stop -> stop.getStationId().equals(stationId)).findAny().orElse(null);
	}
	
	public RouteStop getStop(int position) {
		if (stops == null) return null;
		return stops.get(position);
	}
	
	public int getAmountOfStops() {
		return stops != null ? stops.size() : 0;
	}
	
	public RouteStop getFirstStop() {
		return stops != null && !stops.isEmpty() ? stops.get(0) : null;
	}
	
	public RouteStop getLastStop() {
		return stops != null && !stops.isEmpty() ? stops.get(stops.size() - 1) : null;
	}
	
	public List<RouteStop> getStops() {
		return Collections.unmodifiableList(stops != null ? stops : new ArrayList<>());
	}
	
	public LineScedule getScedule() {
		return scedule;
	}
	
	public void addInformation(Information information) {
		if (informations == null) informations = new ArrayList<>();
		informations.add(information);
	}
	
	public void removeInformation(Information information) {
		if (informations == null) return;
		informations.remove(information);
		if (informations.isEmpty()) informations = null;
	}
	
	public void removeInformation(int index) {
		if (informations == null) return;
		informations.remove(index);
		if (informations.isEmpty()) informations = null;
	}
	
	public Information getInformation(int index) {
		if (informations == null) return null;
		return informations.get(index);
	}
	
	public int getInformationIndex(Information information) {
		if (information == null) return -1;
		return informations.indexOf(information);
	}
	
	public List<Information> getInformations() {
		final List<Information> results = informations != null ? informations : new ArrayList<>();
		return results.stream().sorted().collect(Collectors.toUnmodifiableList());
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
	
	@Override
	public Route copy() {
		final Route route = new Route(this.id, this.name, this.type, this.number);
		route.operatorId = this.operatorId;
		if (stops != null) route.stops = this.stops.stream().map(RouteStop::new).toList();
		if (scedule != null) route.scedule = new LineScedule(this.scedule);
		if (informations != null) route.informations = this.informations.stream().map(Information::new).toList();
		if (api != null) route.api = new ApiInformation(this.api);
		return route;
	}

}
