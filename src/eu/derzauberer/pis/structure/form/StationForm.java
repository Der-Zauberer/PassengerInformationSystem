package eu.derzauberer.pis.structure.form;

import java.util.List;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.Location;
import eu.derzauberer.pis.structure.container.Services;

public class StationForm {
	
	private String id;
	private String name;
	private List<PlatformForm> platforms;
	private Address address;
	private Location location;
	private Services services;
	
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
	
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address adress) {
		this.address = adress;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Services getServices() {
		return services;
	}
	
	public void setServices(Services services) {
		this.services = services;
	}

}
