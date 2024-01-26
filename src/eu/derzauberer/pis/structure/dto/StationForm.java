package eu.derzauberer.pis.structure.dto;

import java.util.List;

import eu.derzauberer.pis.structure.model.AddressModel;
import eu.derzauberer.pis.structure.model.LocationModel;
import eu.derzauberer.pis.structure.model.ServicesModel;

public class StationForm {
	
	private String id;
	private String name;
	private List<PlatformForm> platforms;
	private AddressModel address;
	private LocationModel location;
	private ServicesModel services;
	
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
	
	public List<PlatformForm> getPlatforms() {
		return platforms;
	}
	
	public void setPlatforms(List<PlatformForm> platforms) {
		this.platforms = platforms;
	}
	
	public AddressModel getAddress() {
		return address;
	}
	
	public void setAddress(AddressModel adress) {
		this.address = adress;
	}
	
	public LocationModel getLocation() {
		return location;
	}
	
	public void setLocation(LocationModel location) {
		this.location = location;
	}
	
	public ServicesModel getServices() {
		return services;
	}
	
	public void setServices(ServicesModel services) {
		this.services = services;
	}

}
