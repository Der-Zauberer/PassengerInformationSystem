package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import eu.derzauberer.pis.persistence.Entity;
import eu.derzauberer.pis.persistence.Namable;

public class StationModel extends Entity<StationModel> implements Namable {
	
	private final String id;
	private String name;
	private final SortedSet<PlatformModel> platforms;
	private AddressModel address;
	private LocationModel location;
	private ServicesModel services;
	private ApiInformationModel api;
	
	public StationModel(String name) {
		this(Namable.nameToId(name), name);
	}
	
	@ConstructorProperties({ "id", "name" })
	public StationModel(String id, String name) {
		this.id = id;
		this.name = name;
		platforms = new TreeSet<>();
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
	
	public SortedSet<PlatformModel> getPlatforms() {
		return platforms;
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
	
	public Optional<LocationModel> getLocation() {
		return Optional.ofNullable(location);
	}
	
	public LocationModel getOrCreateLocation() {
		if (location == null) location = new LocationModel();
		return location;
	}
	
	public void setLocation(LocationModel location) {
		this.location = location;
	}
	
	public Optional<ServicesModel> getServices() {
		return Optional.ofNullable(services);
	}
	
	public ServicesModel getOrCreateServices() {
		if (services == null) services = new ServicesModel();
		return services;
	}
	
	public void setServices(ServicesModel services) {
		this.services = services;
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
	public StationModel copy() {
		final StationModel station = new StationModel(this.id, this.name);
		if (platforms != null) this.platforms.stream().map(PlatformModel::new).forEach(station.getPlatforms()::add);
		if (address != null) station.address = new AddressModel(this.address);
		if (location != null) station.location = new LocationModel(this.location);
		if (services != null) station.services = new ServicesModel(this.services);
		if (api != null) station.api = new ApiInformationModel(this.api);
		return station;
	}

}
