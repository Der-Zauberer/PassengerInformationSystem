package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RouteModel extends EntityModel<RouteModel> implements NameEntityModel {
	
	private final String id;
	private String name;
	private String typeId;
	private int number;
	private String operatorId;
	private List<RouteStopModel> stops;
	private LineSceduleModel scedule;
	private List<InformationModel> informations;
	private ApiInformationModel api;
	
	@ConstructorProperties({ "id", "name", "typeId" , "number"})
	public RouteModel(String id, String name, String typeId, int number) {
		this.id = id;
		this.name = name;
		this.typeId = typeId;
		this.number = number;
		this.scedule = new LineSceduleModel();
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
	
	public String getTypeId() {
		return typeId;
	}
	
	public void setTypeId(String typeId) {
		this.typeId = typeId;
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
	
	public void addStop(RouteStopModel stop) {
		if (stops == null) stops = new ArrayList<>();
		stops.add(stop);
	}
	
	public void addStop(int position, RouteStopModel stop) {
		if (stops == null) { 
			final List<RouteStopModel> newStops = new ArrayList<>();
			newStops.set(position, stop);
			stops = newStops;
		} else {
			stops.set(position, stop);
		}
	}
	
	public void removeStop(RouteStopModel stop) {
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
	
	public RouteStopModel getStop(String stationId) {
		if (stops == null) return null;
		return stops.stream().filter(stop -> stop.getStationId().equals(stationId)).findAny().orElse(null);
	}
	
	public RouteStopModel getStop(int position) {
		if (stops == null) return null;
		return stops.get(position);
	}
	
	public int getAmountOfStops() {
		return stops != null ? stops.size() : 0;
	}
	
	public RouteStopModel getFirstStop() {
		return stops != null && !stops.isEmpty() ? stops.get(0) : null;
	}
	
	public RouteStopModel getLastStop() {
		return stops != null && !stops.isEmpty() ? stops.get(stops.size() - 1) : null;
	}
	
	public List<RouteStopModel> getStops() {
		return Collections.unmodifiableList(stops != null ? stops : new ArrayList<>());
	}
	
	public LineSceduleModel getScedule() {
		return scedule;
	}
	
	public void addInformation(InformationModel information) {
		if (informations == null) informations = new ArrayList<>();
		informations.add(information);
	}
	
	public void removeInformation(InformationModel information) {
		if (informations == null) return;
		informations.remove(information);
		if (informations.isEmpty()) informations = null;
	}
	
	public void removeInformation(int index) {
		if (informations == null) return;
		informations.remove(index);
		if (informations.isEmpty()) informations = null;
	}
	
	public InformationModel getInformation(int index) {
		if (informations == null) return null;
		return informations.get(index);
	}
	
	public int getInformationIndex(InformationModel information) {
		if (information == null) return -1;
		return informations.indexOf(information);
	}
	
	public List<InformationModel> getInformations() {
		final List<InformationModel> results = informations != null ? informations : new ArrayList<>();
		return results.stream().sorted().collect(Collectors.toUnmodifiableList());
	}
	
	public Optional<ApiInformationModel> getApiInformation() {
		return Optional.ofNullable(api);
	}
	
	public ApiInformationModel getOrCreateApiInformation() {
		if (api == null) api = new ApiInformationModel();
		return api;
	}
	
	public void setApiInformation(ApiInformationModel api) {
		this.api = api;
	}
	
	@Override
	public RouteModel copy() {
		final RouteModel route = new RouteModel(this.id, this.name, this.typeId, this.number);
		route.operatorId = this.operatorId;
		if (stops != null) route.stops = this.stops.stream().map(RouteStopModel::new).toList();
		if (scedule != null) route.scedule = new LineSceduleModel(this.scedule);
		if (informations != null) route.informations = this.informations.stream().map(InformationModel::new).toList();
		if (api != null) route.api = new ApiInformationModel(this.api);
		return route;
	}

}
