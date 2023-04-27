package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.util.Set;
import java.util.TreeSet;

import eu.derzauberer.pis.util.Entity;

public class Station implements Entity<Station> {
	
	private final String id;
	private final String name;
	private TreeSet<Platform> platforms;
	private Adress adress;
	private Location location;
	private StationServices services;
	private ApiInformation api;

	public Station(String name) {
		this(Entity.nameToId(name), name);
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
	
	public Adress getAdress() {
		return adress;
	}
	
	public Adress getOrCreateAdress() {
		if (adress == null) adress = new Adress();
		return adress;
	}
	
	public void setAdress(Adress adress) {
		this.adress = adress;
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

}
