package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

public class TrainType implements Entity {

	private final String id;
	private final String name;
	
	@ConstructorProperties({"id", "name"})
	public TrainType(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}

}
