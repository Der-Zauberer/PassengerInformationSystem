package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

public class LineStopModel {
	
	private final String stationId;
	private final int platform;
	private int changedPlatform;
	private final String platfromArea;
	private String changedPlatfromArea;
	private final LocalDateTime arrival;
	private final LocalDateTime departure;
	private int delay;
	private boolean cancelled;
	
	public LineStopModel(String stationId, int platform, String platfromArea, LocalDateTime departure) {
		this.stationId = stationId;
		this.platform = platform;
		this.platfromArea = platfromArea;
		this.arrival = departure;
		this.departure = departure;
	}
	
	@ConstructorProperties({"stationId", "platform", "platfromArea", "arrival", "departure"})
	public LineStopModel(String stationId, int platform, String platfromArea, LocalDateTime arrival, LocalDateTime departure) {
		this.stationId = stationId;
		this.platform = platform;
		this.platfromArea = platfromArea;
		this.arrival = arrival;
		this.departure = departure;
	}
	
	public LineStopModel(LineStopModel lineStop) {
		this.stationId = lineStop.stationId;
		this.platform = lineStop.platform;
		this.changedPlatform = lineStop.changedPlatform;
		this.platfromArea = lineStop.platfromArea;
		this.changedPlatfromArea = lineStop.changedPlatfromArea;
		this.arrival = lineStop.arrival;
		this.departure = lineStop.departure;
		this.delay = lineStop.delay;
		this.cancelled = lineStop.cancelled;
	}
	
	public String getStationId() {
		return stationId;
	}
	
	public int getPlatform() {
		return platform;
	}
	
	public int getChangedPlatform() {
		return changedPlatform;
	}
	
	public void setChangedPlatform(int changedPlatform) {
		this.changedPlatform = changedPlatform;
	}
	
	public String getPlatfromArea() {
		return platfromArea;
	}
	
	public String getChangedPlatfromArea() {
		return changedPlatfromArea;
	}
	
	public void setChangedPlatfromArea(String changedPlatfromArea) {
		this.changedPlatfromArea = changedPlatfromArea;
	}
	
	public LocalDateTime getArrival() {
		return arrival;
	}
	
	public LocalDateTime getDeparture() {
		return departure;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}

}
