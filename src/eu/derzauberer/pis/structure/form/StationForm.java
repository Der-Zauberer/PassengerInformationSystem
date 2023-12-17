package eu.derzauberer.pis.structure.form;

import java.util.SortedSet;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.Location;
import eu.derzauberer.pis.structure.container.Platform;
import eu.derzauberer.pis.structure.container.StationServices;

public class StationForm {
	
	private String id;
	private String name;
	private SortedSet<Platform> platforms;
	private Address address;
	private Location location;
	private StationServices services;
	
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
	
	public Address getOrCreateAdress() {
		if (address == null) address = new Address();
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
	
	public StationServices getServices() {
		return services;
	}
	
	public StationServices getorCreateServices() {
		if (services == null) services = new StationServices();
		return services;
	}
	
	public void setServices(StationServices services) {
		this.services = services;
	}

}