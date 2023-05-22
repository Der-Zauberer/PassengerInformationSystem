package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

public class RouteStop {
	
	private final String stationId;
	private final int platform;
	private final String platfromArea;
	private final int arrivalMinutesSinceStart;
	private final int departureMinutesSinceStart;
	
	public RouteStop(String stationId, int platform, String platfromArea, int departureMinutesSinceStart) {
		this.stationId = stationId;
		this.platform = platform;
		this.platfromArea = platfromArea;
		this.arrivalMinutesSinceStart = departureMinutesSinceStart;
		this.departureMinutesSinceStart = departureMinutesSinceStart;
	}
	
	@ConstructorProperties({"stationId", "platform", "platfromArea", "arrivalMinutesSinceStart", "departureMinutesSinceStart"})
	public RouteStop(String stationId, int platform, String platfromArea, int arrivalMinutesSinceStart, int departureMinutesSinceStart) {
		this.stationId = stationId;
		this.platform = platform;
		this.platfromArea = platfromArea;
		this.arrivalMinutesSinceStart = arrivalMinutesSinceStart;
		this.departureMinutesSinceStart = departureMinutesSinceStart;
	}
	
	public String getStationId() {
		return stationId;
	}
	
	public int getPlatform() {
		return platform;
	}
	
	public String getPlatfromArea() {
		return platfromArea;
	}
	
	public int getArrivalMinutesSinceStart() {
		return arrivalMinutesSinceStart;
	}
	
	public int getDepartureMinutesSinceStart() {
		return departureMinutesSinceStart;
	}

}
