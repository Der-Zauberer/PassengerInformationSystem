package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

import eu.derzauberer.pis.enums.TransportationClassification;
import eu.derzauberer.pis.enums.TransportationType;

public class Type implements Entity<Type>, NameEntity {

	private final String id;
	private final String name;
	private final TransportationType type;
	private final TransportationClassification classification;
	private int backgroundColor;
	private int textColor;
	private ApiInformation api;
	
	@ConstructorProperties({ "id", "name", "type", "classification" })
	public Type(String id, String name, TransportationType type, TransportationClassification classification) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.classification = classification;
		this.backgroundColor = 0x000000;
		this.textColor = 0xffffff;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public TransportationType getType() {
		return type;
	}

	public TransportationClassification getClassification() {
		return classification;
	}
	
	public int getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
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
