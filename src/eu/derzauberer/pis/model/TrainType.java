package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

public class TrainType implements Entity {
	
	public enum TrainClassifican {
		FREIGHT,
		PASSENGER_REGIONAL,
		PASSENGER_DISTANCE
	}

	private final String id;
	private final String name;
	private final TrainClassifican classification;
	
	@ConstructorProperties({"id", "name", "classification"})
	public TrainType(String id, String name, TrainClassifican classification) {
		this.id = id;
		this.name = name;
		this.classification = classification;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public TrainClassifican getClassification() {
		return classification;
	}

}
