package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;
import java.util.Optional;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.ApiInformation;
import eu.derzauberer.pis.structure.container.Color;

public class Operator extends Entity<Operator> implements NameEntity {

	private final String id;
	private String name;
	private Address address;
	private Color color;
	private ApiInformation api;
	
	public Operator(String name) {
		this(NameEntity.nameToId(name), name);
	}
	
	@ConstructorProperties({ "id", "name" })
	public Operator(String id, String name) {
		this.id = id;
		this.name = name;
		this.color = new Color();
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
	
	public Optional<Address> getAddress() {
		return Optional.ofNullable(address);
	}
	
	public Address getOrCreateAddress() {
		if (address == null) address = new Address();
		return address;
	}
	
	public void setAddress(Address adress) {
		this.address = adress;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Optional<ApiInformation> getApiInformation() {
		return Optional.ofNullable(api);
	}
	
	public ApiInformation getOrCreateApiInformation() {
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
		operator.color = new Color(this.color);
		if (api != null) operator.api = new ApiInformation(this.api);
		return operator;
	}

}
