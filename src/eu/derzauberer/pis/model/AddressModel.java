package eu.derzauberer.pis.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressModel {
	
	private String name;
	private String street;
	private Integer postalCode;
	private String city;
	private String state;
	private String country;
	private Integer telephoneNumber;
	private String email;
	
	public AddressModel() {}
	
	public AddressModel(AddressModel address) {
		this.name = address.name;
		this.street = address.street;
		this.postalCode = address.postalCode;
		this.city = address.city;
		this.state = address.state;
		this.country = address.country;
		this.telephoneNumber = address.telephoneNumber;
		this.email = address.email;
	}
	
}
