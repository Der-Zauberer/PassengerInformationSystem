package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalTime;

public class LineStopLiveData extends LineStop {
	
	private int changedPlatform;
	private String changedPlatfromArea;
	private int delay;
	private boolean cancelled;
	private String information;
	
	public LineStopLiveData(String station, int platform, String platfromArea, LocalTime departure) {
		super(station, platform, platfromArea, departure);
		this.delay = 0;
		this.cancelled = false;
	}
	
	@ConstructorProperties({"station", "platform", "platfromArea", "arrival", "departure"})
	public LineStopLiveData(String station, int platform, String platfromArea, LocalTime arrival, LocalTime departure) {
		super(station, platform, platfromArea, arrival, departure);
		this.delay = 0;
		this.cancelled = false;
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
	
	public int getDelay() {
		return delay;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public String getInformation() {
		return information;
	}
	
	public void setInformation(String information) {
		this.information = information;
	}
	
}
