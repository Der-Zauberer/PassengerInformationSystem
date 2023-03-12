package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Line implements Entity<Long>{

	private final Long id;
	private TrainType type;
	private int number;
	private LocalDate date;
	private String operator;
	private String driver;
	private boolean cancelled;
	private Integer position;
	private final List<LineStop> stops = new ArrayList<>();
	
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
	
	public List<LineStop> getStops() {
		return stops;
	}

}
