package eu.derzauberer.pis.structure.data;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.ApiInformation;
import eu.derzauberer.pis.structure.container.Color;

public class OperatorData {
	
	private String id;
	private String name;
	private Address address;
	private Color color;
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
	
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Color getOrCreateColor() {
		if (color == null) color = new Color();
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
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
