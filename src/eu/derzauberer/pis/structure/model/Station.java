package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import eu.derzauberer.pis.structrue.container.Address;
import eu.derzauberer.pis.structrue.container.ApiInformation;
import eu.derzauberer.pis.structrue.container.Location;
import eu.derzauberer.pis.structrue.container.Platform;
import eu.derzauberer.pis.structrue.container.StationServices;

public class Station extends Entity<Station> implements NameEntity {
	
	private final String id;
	private final String name;
	private TreeSet<Platform> platforms;
	private Address address;
	private Location location;
	private StationServices services;
	private ApiInformation api;
	
	public Station(String name) {
		this(NameEntity.nameToId(name), name);
	}
	
	@ConstructorProperties({ "id", "name" })
	public Station(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public Set<Platform> getPlatforms() {
		return platforms;
	}
	
	public void setPlatforms(TreeSet<Platform> platforms) {
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
	
	@Override
	public Station copy() {
		final Station station = new Station(this.id, this.name);
		if (platforms != null) station.platforms = this.platforms.stream().map(Platform::new).collect(Collectors.toCollection(TreeSet::new));
		if (address != null) station.address = new Address(this.address);
		if (location != null) station.location = new Location(this.location);
		if (services != null) station.services = new StationServices(this.services);
		if (api != null) station.api = new ApiInformation(this.api);
		return station;
	}

}
