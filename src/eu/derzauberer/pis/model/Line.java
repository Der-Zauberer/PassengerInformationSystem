package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

public abstract class Line<T extends LineStop> {
	
	private final String id;
	private TrainType type;
	private int number;
	private String operator;
	private final List<T> stops = new ArrayList<>();
	private ApiInformation api;
	
	@ConstructorProperties({ "id", "type", "number" })
	public Line(String id, TrainType type, int number) {
		this.id = id;
		this.type = type;
		this.number = number;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return type.getId() + number;
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public List<T> getStops() {
		return stops;
	}
	
	public ApiInformation getApiInformation() {
		return api;
	}
	
	public ApiInformation getOrCreateApiInformtion() {
		if (api == null) api = new ApiInformation();
		return api;
	}
	
	public void setApiInformation(ApiInformation api) {
		this.api = api;
	}

}
