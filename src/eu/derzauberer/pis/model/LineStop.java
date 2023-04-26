package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalTime;

public class LineStop {
	
	private final String station;
	private final int platform;
	private final String platfromArea;
	private final LocalTime arrival;
	private final LocalTime departure;
	
	public LineStop(String station, int platform, String platfromArea, LocalTime departure) {
		this.station = station;
		this.platform = platform;
		this.platfromArea = platfromArea;
		this.arrival = departure;
		this.departure = departure;
	}
	
	@ConstructorProperties({"station", "platform", "platfromArea", "arrival", "departure"})
	public LineStop(String station, int platform, String platfromArea, LocalTime arrival, LocalTime departure) {
		this.station = station;
		this.platform = platform;
		this.platfromArea = platfromArea;
		this.arrival = arrival;
		this.departure = departure;
	}
	
	public String getStation() {
		return station;
	}
	
	public int getPlatform() {
		return platform;
	}
	
	public String getPlatfromArea() {
		return platfromArea;
	}
	
	public LocalTime getArrival() {
		return arrival;
	}
	
	public LocalTime getDeparture() {
		return departure;
	}

}
