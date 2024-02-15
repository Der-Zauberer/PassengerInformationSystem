package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

import lombok.Getter;

@Getter
public class RouteStopModel {
	
	private final String stationId;
	private final int platform;
	private final String platfromArea;
	private final int arrivalMinutesSinceStart;
	private final int departureMinutesSinceStart;
	
	public RouteStopModel(String stationId, int platform, String platfromArea, int departureMinutesSinceStart) {
		this.stationId = stationId;
		this.platform = platform;
		this.platfromArea = platfromArea;
		this.arrivalMinutesSinceStart = departureMinutesSinceStart;
		this.departureMinutesSinceStart = departureMinutesSinceStart;
	}
	
	@ConstructorProperties({"stationId", "platform", "platfromArea", "arrivalMinutesSinceStart", "departureMinutesSinceStart"})
	public RouteStopModel(String stationId, int platform, String platfromArea, int arrivalMinutesSinceStart, int departureMinutesSinceStart) {
		this.stationId = stationId;
		this.platform = platform;
		this.platfromArea = platfromArea;
		this.arrivalMinutesSinceStart = arrivalMinutesSinceStart;
		this.departureMinutesSinceStart = departureMinutesSinceStart;
	}
	
	public RouteStopModel(RouteStopModel routeStop) {
		this.stationId = routeStop.stationId;
		this.platform = routeStop.platform;
		this.platfromArea = routeStop.platfromArea;
		this.arrivalMinutesSinceStart = routeStop.arrivalMinutesSinceStart;
		this.departureMinutesSinceStart = routeStop.departureMinutesSinceStart;
	}

}
