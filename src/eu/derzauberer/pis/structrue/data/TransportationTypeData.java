package eu.derzauberer.pis.structrue.data;

import eu.derzauberer.pis.structrue.container.ApiInformation;
import eu.derzauberer.pis.structure.enums.TransportationClassification;
import eu.derzauberer.pis.structure.enums.TransportationVehicle;

public class TransportationTypeData {
	
	private String id;
	private String name;
	private TransportationVehicle vehicle;
	private TransportationClassification classification;
	private String backgroundColor;
	private String textColor;
	private ApiInformation api;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public TransportationVehicle getVehicle() {
		return vehicle;
	}
	
	public void setVehicle(TransportationVehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	public TransportationClassification getClassification() {
		return classification;
	}
	
	public void setClassification(TransportationClassification classification) {
		this.classification = classification;
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
	
	public ApiInformation getApi() {
		return api;
	}
	
	public void setApi(ApiInformation api) {
		this.api = api;
	}

}