package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "name", "adress"})
public class TrainOperator implements Entity<String> {

	private final String id;
	private final String name;
	private final Adress adress = new Adress();
	
	public TrainOperator(String name) {
		this(Entity.nameToId(name), name);
	}
	
	@ConstructorProperties({"id", "name"})
	public TrainOperator(String id, String name) {
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
	
	public Adress getAdress() {
		return adress;
	}

}
