package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

public class LineStop {
	
	private final String stationId;
	private final int platform;
	private final String platfromArea;
	private final LocalDateTime arrival;
	private final LocalDateTime departure;
	
	public LineStop(String stationId, int platform, String platfromArea, LocalDateTime departure) {
		this.stationId = stationId;
		this.platform = platform;
		this.platfromArea = platfromArea;
		this.arrival = departure;
		this.departure = departure;
	}
	
	@ConstructorProperties({"stationId", "platform", "platfromArea", "arrival", "departure"})
	public LineStop(String stationId, int platform, String platfromArea, LocalDateTime arrival, LocalDateTime departure) {
		this.stationId = stationId;
		this.platform = platform;
		this.platfromArea = platfromArea;
		this.arrival = arrival;
		this.departure = departure;
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
	
	public LocalDateTime getArrival() {
		return arrival;
	}
	
	public LocalDateTime getDeparture() {
		return departure;
	}

}
