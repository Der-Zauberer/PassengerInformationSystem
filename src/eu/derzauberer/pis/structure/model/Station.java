package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.ApiInformation;
import eu.derzauberer.pis.structure.container.Location;
import eu.derzauberer.pis.structure.container.Platform;
import eu.derzauberer.pis.structure.container.Services;

public class Station extends Entity<Station> implements NameEntity {
	
	private final String id;
	private String name;
	private SortedSet<Platform> platforms;
	private Address address;
	private Location location;
	private Services services;
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	public SortedSet<Platform> getPlatforms() {
		return platforms;
	}
	
	public void setPlatforms(SortedSet<Platform> platforms) {
		this.platforms = platforms;
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
	
	public Optional<Location> getLocation() {
		return Optional.ofNullable(location);
	}
	
	public Location getOrCreateLocation() {
		if (location == null) location = new Location();
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Optional<Services> getServices() {
		return Optional.ofNullable(services);
	}
	
	public Services getOrCreateServices() {
		if (services == null) services = new Services();
		return services;
	}
	
	public void setServices(Services services) {
		this.services = services;
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
	public Station copy() {
		final Station station = new Station(this.id, this.name);
		if (platforms != null) station.platforms = this.platforms.stream().map(Platform::new).collect(Collectors.toCollection(TreeSet::new));
		if (address != null) station.address = new Address(this.address);
		if (location != null) station.location = new Location(this.location);
		if (services != null) station.services = new Services(this.services);
		if (api != null) station.api = new ApiInformation(this.api);
		return station;
	}

}
