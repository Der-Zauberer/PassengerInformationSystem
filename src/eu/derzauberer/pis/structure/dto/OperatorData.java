package eu.derzauberer.pis.structure.dto;

import eu.derzauberer.pis.structure.model.AddressModel;
import eu.derzauberer.pis.structure.model.ApiInformationModel;
import eu.derzauberer.pis.structure.model.ColorModel;

public class OperatorData {
	
	private String id;
	private String name;
	private AddressModel address;
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
	
	public AddressModel getAddress() {
		return address;
	}
	
	public void setAddress(AddressModel address) {
		this.address = address;
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
