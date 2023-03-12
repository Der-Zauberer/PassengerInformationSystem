package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalTime;

public class LineStop {

	private final String station;
	private int platform;
	private String platfromArea;
	private LocalTime arrival;
	private LocalTime departure;
	private boolean cancelled;
	private int delay;
	private int changedPlatform;
	private String changedPlatfromArea;
	private String information;
	
	public LineStop(String station, int platform, LocalTime departure) {
		this.station = station;
		this.platform = platform;
		this.arrival = departure;
		this.departure = departure;
		this.cancelled = false;
		this.delay = 0;
		this.changedPlatform = 0;
	}
	
	@ConstructorProperties({"station", "platform", "arrival", "departure"})
	public LineStop(String station, int platform, LocalTime arrival, LocalTime departure) {
		this.station = station;
		this.platform = platform;
		this.arrival = arrival;
		this.departure = departure;
		this.cancelled = false;
		this.delay = 0;
		this.changedPlatform = 0;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public String getPlatfromArea() {
		return platfromArea;
	}

	public void setPlatfromArea(String platfromArea) {
		this.platfromArea = platfromArea;
	}

	public LocalTime getArrival() {
		return arrival;
	}

	public void setArrival(LocalTime arrival) {
		this.arrival = arrival;
	}

	public LocalTime getDeparture() {
		return departure;
	}

	public void setDeparture(LocalTime departure) {
		this.departure = departure;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getChangedPlatform() {
		return changedPlatform;
	}

	public void setChangedPlatform(int changedPlatform) {
		this.changedPlatform = changedPlatform;
	}

	public String getChangedPlatfromArea() {
		return changedPlatfromArea;
	}

	public void setChangedPlatfromArea(String changedPlatfromArea) {
		this.changedPlatfromArea = changedPlatfromArea;
	}

	public String getStation() {
		return station;
	}
	
	public String getInformation() {
		return information;
	}
	
	public void setInformation(String information) {
		this.information = information;
	}
	
}
