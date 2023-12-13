package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.ApiInformation;

public class Operator extends Entity<Operator> implements NameEntity {

	private final String id;
	private String name;
	private Address address;
	private int backgorundColor;
	private int textColor;
	private ApiInformation api;
	
	public Operator(String name) {
		this(NameEntity.nameToId(name), name);
	}
	
	@ConstructorProperties({ "id", "name" })
	public Operator(String id, String name) {
		this.id = id;
		this.name = name;
		this.backgorundColor = 0x000000;
		this.textColor = 0xffffff;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public Address getOrCreateAdress() {
		if (address == null) address = new Address();
		return address;
	}
	
	public void setAddress(Address adress) {
		this.address = adress;
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
	
	public ApiInformation getApiInformation() {
		return api;
	}
	
	public ApiInformation getOrCreateApi() {
		if (api == null) api = new ApiInformation();
		return api;
	}
	
	public void setApiInformation(ApiInformation api) {
		this.api = api;
	}
	
	@Override
	public Operator copy() {
		final Operator operator = new Operator(this.id, this.name);
		if (address != null) operator.address = new Address(this.address);
		operator.backgorundColor = this.backgorundColor;
		operator.textColor = this.textColor;
		if (api != null) operator.api = new ApiInformation(this.api);
		return operator;
	}

}
