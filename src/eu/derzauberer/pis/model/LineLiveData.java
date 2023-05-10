package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "type", "number", "operator", "driver", "cancelled", "stops", "position", "api" })
public class LineLiveData extends Line<LineStopLiveData> implements Entity<LineLiveData> {

	private String driver;
	private boolean cancelled;
	private Integer position;
	
	@ConstructorProperties({ "id", "type", "number"})
	public LineLiveData(String id, TrainType type, int number) {
		super(id, type, number);
		cancelled = false;
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

}
