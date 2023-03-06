package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.derzauberer.pis.serialization.TimeDeserializer;
import eu.derzauberer.pis.serialization.TimeSerializer;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"station", "platform", "platfromArea", "arrival", "departure", "cancelled", "delay", "changedPlatform", "changedPlatfromArea"})
public class TrainStop {

	private final String station;
	private int platform;
	private String platfromArea;
	@JsonSerialize(using = TimeSerializer.class)
	@JsonDeserialize(using = TimeDeserializer.class)
	private LocalTime arrival;
	@JsonSerialize(using = TimeSerializer.class)
	@JsonDeserialize(using = TimeDeserializer.class)
	private LocalTime departure;
	private boolean cancelled;
	private int delay;
	private int changedPlatform;
	private String changedPlatfromArea;
	
	public TrainStop(String station, int platform, LocalTime departure) {
		this.station = station;
		this.platform = platform;
		this.arrival = departure;
		this.departure = departure;
		this.cancelled = false;
		this.delay = 0;
		this.changedPlatform = 0;
	}
	
	@ConstructorProperties({"station", "arrival", "departure"})
	public TrainStop(String station, int platform, LocalTime arrival, LocalTime departure) {
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
	
}
