package eu.derzauberer.pis.structure.dto;

import eu.derzauberer.pis.structure.enums.TransportationClassification;
import eu.derzauberer.pis.structure.enums.TransportationVehicle;
import eu.derzauberer.pis.structure.model.ApiInformationModel;
import eu.derzauberer.pis.structure.model.ColorModel;

public class TransportationTypeData {
	
	private String id;
	private String name;
	private String description;
	private TransportationVehicle vehicle;
	private TransportationClassification classification;
	private ColorModel color;
	private ApiInformationModel api;
	
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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
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
	
	public ColorModel getColor() {
		return color;
	}
	
	public ColorModel getOrCreateColor() {
		if (color == null) color = new ColorModel();
		return color;
	}
	
	public void setColor(ColorModel color) {
		this.color = color;
	}
	
	public ApiInformationModel getApiInformation() {
		return api;
	}
	
	public ApiInformationModel getOrCreateApiInformation() {
		if (api == null) api = new ApiInformationModel();
		return api;
	}
	
	public void setApiInformation(ApiInformationModel api) {
		this.api = api;
	}

}
