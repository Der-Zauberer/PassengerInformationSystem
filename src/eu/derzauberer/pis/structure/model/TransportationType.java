package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;

import eu.derzauberer.pis.structrue.container.ApiInformation;
import eu.derzauberer.pis.structure.enums.TransportationClassification;
import eu.derzauberer.pis.structure.enums.TransportationVehicle;

public class TransportationType extends Entity<TransportationType> implements NameEntity {

	private final String id;
	private final String name;
	private final TransportationVehicle vehicle;
	private final TransportationClassification classification;
	private String backgroundColor;
	private String textColor;
	private ApiInformation api;
	
	@ConstructorProperties({ "id", "name", "vehicle", "classification" })
	public TransportationType(String id, String name, TransportationVehicle vehicle, TransportationClassification classification) {
		this.id = id;
		this.name = name;
		this.vehicle = vehicle;
		this.classification = classification;
		this.backgroundColor = "#000000";
		this.textColor = "#ffffff";
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
	
	public String getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public String getTextColor() {
		return textColor;
	}
	
	public void setTextColor(String textColor) {
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
