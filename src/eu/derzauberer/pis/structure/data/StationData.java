package eu.derzauberer.pis.structure.data;

import java.util.SortedSet;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.ApiInformation;
import eu.derzauberer.pis.structure.container.Location;
import eu.derzauberer.pis.structure.container.Platform;
import eu.derzauberer.pis.structure.container.StationServices;

public class StationData {
	
	private String id;
	private String name;
	private SortedSet<Platform> platforms;
	private Address address;
	private Location location;
	private StationServices services;
	private ApiInformation api;
	
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
	
	public SortedSet<Platform> getPlatforms() {
		return platforms;
	}
	
	public void setPlatforms(SortedSet<Platform> platforms) {
		this.platforms = platforms;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public Address getOrCreateAddress() {
		if (address == null) address = new Address();
		return address;
	}
	
	public void setAddress(Address adress) {
		this.address = adress;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Location getOrCreateLocation() {
		if (location == null) location = new Location();
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public StationServices getServices() {
		return services;
	}
	
	public StationServices getOrCreateServices() {
		if (services == null) services = new StationServices();
		return services;
	}
	
	public void setServices(StationServices services) {
		this.services = services;
	}
	
	public ApiInformation getApiInformation() {
		return api;
	}
	
	public ApiInformation getOrCreateApiInformation() {
		if (api == null) api = new ApiInformation();
		return api;
	}
	
	public void setApiInformation(ApiInformation api) {
		this.api = api;
	}

}
