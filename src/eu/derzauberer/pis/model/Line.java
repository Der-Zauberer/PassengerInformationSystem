package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.derzauberer.pis.serialization.DateDeserializer;
import eu.derzauberer.pis.serialization.DateSerializer;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "type", "number", "date", "operator", "driver", "cancelled", "position", "stops"})
public class Line implements Entity<Long>{

	private final Long id;
	private TrainType type;
	private int number;
	@JsonSerialize(using = DateSerializer.class)
	@JsonDeserialize(using = DateDeserializer.class)
	private LocalDate date;
	private String operator;
	private String driver;
	private boolean cancelled;
	private Integer position;
	private final List<TrainStop> stops = new ArrayList<>();
	
	@ConstructorProperties({"id", "type", "number"})
	public Line(Long id, TrainType type, int number) {
		this.id = id;
		this.type = type;
		this.number = number;
		this.date = LocalDate.now();
	}
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return type.getToken() + number;
	}

	public TrainType getType() {
		return type;
	}

	public void setType(TrainType type) {
		this.type = type;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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
	
	public void setPosition(Integer trainPosition) {
		this.position = trainPosition;
	}
	
	public List<TrainStop> getStops() {
		return stops;
	}

}
