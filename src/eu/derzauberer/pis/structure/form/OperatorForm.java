package eu.derzauberer.pis.structure.form;

import eu.derzauberer.pis.structure.container.Address;

public class OperatorForm {
	
	private String id;
	private String name;
	private Address address;
	private int backgorundColor;
	private int textColor;
	
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
	
	public int getBackgorundColor() {
		return backgorundColor;
	}
	
	public void setBackgorundColor(int backgorundColor) {
		this.backgorundColor = backgorundColor;
	}
	
	public int getTextColor() {
		return textColor;
	}
	
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

}
