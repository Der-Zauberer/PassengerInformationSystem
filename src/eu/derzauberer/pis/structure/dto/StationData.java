package eu.derzauberer.pis.structure.dto;

import java.util.SortedSet;

import eu.derzauberer.pis.structure.model.AddressModel;
import eu.derzauberer.pis.structure.model.ApiInformationModel;
import eu.derzauberer.pis.structure.model.LocationModel;
import eu.derzauberer.pis.structure.model.PlatformModel;
import eu.derzauberer.pis.structure.model.ServicesModel;

public class StationData {
	
	private String id;
	private String name;
	private SortedSet<PlatformModel> platforms;
	private AddressModel address;
	private LocationModel location;
	private ServicesModel services;
	private ApiInformationModel api;
	
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
	
	public SortedSet<PlatformModel> getPlatforms() {
		return platforms;
	}
	
	public void setPlatforms(SortedSet<PlatformModel> platforms) {
		this.platforms = platforms;
	}
	
	public AddressModel getAddress() {
		return address;
	}
	
	public AddressModel getOrCreateAddress() {
		if (address == null) address = new AddressModel();
		return address;
	}
	
	public void setAddress(AddressModel adress) {
		this.address = adress;
	}
	
	public LocationModel getLocation() {
		return location;
	}
	
	public LocationModel getOrCreateLocation() {
		if (location == null) location = new LocationModel();
		return location;
	}
	
	public void setLocation(LocationModel location) {
		this.location = location;
	}
	
	public ServicesModel getServices() {
		return services;
	}
	
	public ServicesModel getOrCreateServices() {
		if (services == null) services = new ServicesModel();
		return services;
	}
	
	public void setServices(ServicesModel services) {
		this.services = services;
	}
	
	public ApiInformationModel getApiInformation() {
		return api;
	}
	
	public ApiInformationModel getOrCreateApiInformation() {
		if (api == null) api = new ApiInformationModel();
		return api;
	}
	
	public void setApiInformation(ApiInformationModel api) {
		this.api = api;
	}

}
