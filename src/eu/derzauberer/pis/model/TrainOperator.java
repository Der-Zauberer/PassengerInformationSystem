package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "name", "primaryColor", "secondaryColor", "adress"})
public class TrainOperator implements Entity<String> {

	private final String id;
	private String name;
	private int primaryColor;
	private int secondaryColor;
	private final Adress adress = new Adress();
	
	public TrainOperator(String name) {
		this(Entity.nameToId(name), name);
	}
	
	@ConstructorProperties({"id", "name"})
	public TrainOperator(String id, String name) {
		this.id = id;
		this.name = name;
		this.primaryColor = 0;
		this.secondaryColor = 0;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Adress getAdress() {
		return adress;
	}
	
	public int getPrimaryColor() {
		return primaryColor;
	}
	
	public void setPrimaryColor(int primaryColor) {
		this.primaryColor = primaryColor;
	}
	
	public int getSecondaryColor() {
		return secondaryColor;
	}
	
	public void setSecondaryColor(int secondaryColor) {
		this.secondaryColor = secondaryColor;
	}

}
