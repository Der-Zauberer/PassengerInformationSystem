package eu.derzauberer.pis.structure.container;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "street", "postalCode", "city", "state", "country", "telephoneNumber", "email" })
public class Address {
	
	private String name;
	private String street;
	private Integer postalCode;
	private String city;
	private String state;
	private String country;
	private Integer telephoneNumber;
	private String email;
	
	public Address() {}
	
	public Address(Address address) {
		this.name = address.name;
		this.street = address.street;
		this.postalCode = address.postalCode;
		this.city = address.city;
		this.state = address.state;
		this.country = address.country;
		this.telephoneNumber = address.telephoneNumber;
		this.email = address.email;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public Integer getPostalCode() {
		return postalCode;
	}
	
	public void setPostalCode(Integer postalCode) {
		this.postalCode = postalCode;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public Integer getTelephoneNumber() {
		return telephoneNumber;
	}
	
	public void setTelephoneNumber(Integer telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
}