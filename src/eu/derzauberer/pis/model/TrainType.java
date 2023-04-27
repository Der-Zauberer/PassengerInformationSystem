package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

import eu.derzauberer.pis.util.Entity;

public class TrainType implements Entity<TrainType> {
	
	public enum TrainClassifican {
		FREIGHT,
		PASSENGER_REGIONAL,
		PASSENGER_DISTANCE
	}

	private final String id;
	private final String name;
	private final TrainClassifican classification;
	private int backgourndColor;
	private int textColor;
	private ApiInformation api;
	
	@ConstructorProperties({ "id", "name", "classification" })
	public TrainType(String id, String name, TrainClassifican classification) {
		this.id = id;
		this.name = name;
		this.classification = classification;
		this.backgourndColor = 0x000000;
		this.textColor = 0xffffff;
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
	
	public int getBackgourndColor() {
		return backgourndColor;
	}
	
	public void setBackgourndColor(int backgourndColor) {
		this.backgourndColor = backgourndColor;
	}
	
	public int getTextColor() {
		return textColor;
	}
	
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	
	public ApiInformation getApiInformation() {
		return api;
	}
	
	public ApiInformation getOrCreateApiInformation() {
		if (api == null) api = new ApiInformation();
		return api;
	}
	
	public void setApiInformation(ApiInformation api) {
		this.api = api;
	}

}
