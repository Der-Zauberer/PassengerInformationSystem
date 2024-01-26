package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LineModel extends EntityModel<LineModel> implements NameEntityModel {
	
	private final String id;
	private final String routeId;
	private String name;
	private String typeId;
	private int number;
	private String operatorId;
	private String driver;
	private boolean cancelled;
	private Integer position;
	private List<LineStopModel> stops;
	private List<InformationModel> informations;
	private ApiInformationModel api;
	
	private LineModel(String id, RouteModel route, LocalDateTime departure) {
		this.id = id;
		this.routeId = route.getId();
		this.name = route.getName();
		this.typeId = route.getTypeId();
		this.number = route.getNumber();
		this.operatorId = route.getOperatorId();
		if (route.getAmountOfStops() > 0) this.stops = new ArrayList<>();
		for (RouteStopModel stop : route.getStops()) {
			final LineStopModel lineStop = new LineStopModel(
					stop.getStationId(), 
					stop.getPlatform(), 
					stop.getPlatfromArea(),
					departure.plusMinutes(stop.getArrivalMinutesSinceStart()),
					departure.plusMinutes(stop.getDepartureMinutesSinceStart()));
			stops.add(lineStop);
		}
	}
	
	@ConstructorProperties({ "id", "routeId", "typeId", "number" })
	private LineModel(String id, String routeId, String typeId, int number) {
		this.id = id;
		this.routeId = routeId;
		this.typeId = typeId;
		this.number = number;
	}

	@Override
	public String getId() {
		return id;
	}
	
	public String getRouteId() {
		return routeId;
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
	
	public String getDriver() {
		return driver;
	}
	
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public Integer getPosition() {
		return position;
	}
	
	public void setPosition(Integer position) {
		this.position = position;
	}
	
	public void addStop(LineStopModel stop) {
		if (stops == null) stops = new ArrayList<>();
		stops.add(stop);
	}
	
	public void addStop(int position, LineStopModel stop) {
		if (stops == null) { 
			final List<LineStopModel> newStops = new ArrayList<>();
			newStops.set(position, stop);
			stops = newStops;
		} else {
			stops.set(position, stop);
		}
	}
	
	public void removeStop(LineStopModel stop) {
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
	
	public LineStopModel getStop(String stationId) {
		if (stops == null) return null;
		return stops.stream().filter(stop -> stop.getStationId().equals(stationId)).findAny().orElse(null);
	}
	
	public LineStopModel getStop(int position) {
		if (stops == null) return null;
		return stops.get(position);
	}
	
	public int getAmountOfStops() {
		return stops != null ? stops.size() : 0;
	}
	
	public LineStopModel getFirstStop() {
		return stops != null && !stops.isEmpty() ? stops.get(0) : null;
	}
	
	public LineStopModel getLastStop() {
		return stops != null && !stops.isEmpty() ? stops.get(stops.size() - 1) : null;
	}
	
	public List<LineStopModel> getStops() {
		return Collections.unmodifiableList(stops != null ? stops : new ArrayList<>());
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
		if (informations == null) return -1;
		return informations.indexOf(information);
	}
	
	public List<InformationModel> getInformations() {
		final List<InformationModel> results = informations != null ? informations : new ArrayList<>();
		return results.stream().sorted().collect(Collectors.toUnmodifiableList());
	}
	
	public Optional<ApiInformationModel> getApiInformation() {
		return Optional.ofNullable(api);
	}
	
	public ApiInformationModel getOrCreateApiInformtion() {
		if (api == null) api = new ApiInformationModel();
		return api;
	}
	
	public void setApiInformation(ApiInformationModel api) {
		this.api = api;
	}
	
	@Override
	public LineModel copy() {
		final LineModel line = new LineModel(this.id, this.routeId, this.typeId, this.number);
		line.operatorId = this.operatorId;
		line.driver = this.driver;
		line.cancelled = this.cancelled;
		line.position = this.position;
		if (stops != null) line.stops = this.stops.stream().map(LineStopModel::new).toList();
		if (informations != null) line.informations = this.informations.stream().map(InformationModel::new).toList();
		if (api != null) line.api = new ApiInformationModel(this.api);
		return line;
	}

}
