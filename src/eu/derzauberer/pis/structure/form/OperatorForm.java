package eu.derzauberer.pis.structure.form;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.Color;

public class OperatorForm {
	
	private String id;
	private String name;
	private Address address;
	private Color color;
	
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
	
	public void setColor(Color color) {
		this.color = color;
	}

}
