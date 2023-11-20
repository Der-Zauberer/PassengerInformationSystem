package eu.derzauberer.pis.entity;

import java.beans.ConstructorProperties;

import eu.derzauberer.pis.enums.TransportationClassification;
import eu.derzauberer.pis.enums.TransportationVehicle;
import eu.derzauberer.pis.model.ApiInformation;

public class TransportationType implements Entity<TransportationType>, NameEntity {

	private final String id;
	private final String name;
	private final TransportationVehicle vehicle;
	private final TransportationClassification classification;
	private int backgroundColor;
	private int textColor;
	private ApiInformation api;
	
	@ConstructorProperties({ "id", "name", "vehicle", "classification" })
	public TransportationType(String id, String name, TransportationVehicle vehicle, TransportationClassification classification) {
		this.id = id;
		this.name = name;
		this.vehicle = vehicle;
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

	
	public TransportationVehicle getVehicle() {
		return vehicle;
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
