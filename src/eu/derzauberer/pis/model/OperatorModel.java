package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.util.Optional;

import eu.derzauberer.pis.persistence.Entity;
import eu.derzauberer.pis.persistence.Namable;

public class OperatorModel extends Entity<OperatorModel> implements Namable {

	private final String id;
	private String name;
	private AddressModel address;
	private ColorModel color;
	private ApiInformationModel api;
	
	public OperatorModel(String name) {
		this(Namable.nameToId(name), name);
	}
	
	@ConstructorProperties({ "id", "name" })
	public OperatorModel(String id, String name) {
		this.id = id;
		this.name = name;
		this.color = new ColorModel();
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
	
	public Optional<AddressModel> getAddress() {
		return Optional.ofNullable(address);
	}
	
	public AddressModel getOrCreateAddress() {
		if (address == null) address = new AddressModel();
		return address;
	}
	
	public void setAddress(AddressModel adress) {
		this.address = adress;
	}
	
	public ColorModel getColor() {
		return color;
	}
	
	public void setColor(ColorModel color) {
		this.color = color;
	}
	
	public Optional<ApiInformationModel> getApiInformation() {
		return Optional.ofNullable(api);
	}
	
	public ApiInformationModel getOrCreateApiInformation() {
		if (api == null) api = new ApiInformationModel();
		return api;
	}
	
	public void setApiInformation(ApiInformationModel api) {
		this.api = api;
	}
	
	@Override
	public OperatorModel copy() {
		final OperatorModel operator = new OperatorModel(this.id, this.name);
		if (address != null) operator.address = new AddressModel(this.address);
		operator.color = new ColorModel(this.color);
		if (api != null) operator.api = new ApiInformationModel(this.api);
		return operator;
	}

}
