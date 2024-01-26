package eu.derzauberer.pis.structure.dto;

import eu.derzauberer.pis.structure.model.AddressModel;
import eu.derzauberer.pis.structure.model.ColorModel;

public class OperatorForm {
	
	private String id;
	private String name;
	private AddressModel address;
	private ColorModel color;
	
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
	
	public void setColor(ColorModel color) {
		this.color = color;
	}

}
