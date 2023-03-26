package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import eu.derzauberer.pis.util.Entity;

public class Station implements Entity<Station> {
	
	private final String id;
	private final String name;
	private final TreeSet<Platform> platforms = new TreeSet<>();
	private final Adress adress = new Adress();
	private final Location location = new Location(0, 0);
	private final StationServices services = new StationServices();
	private final Map<String, Object> apiIds = new HashMap<>();
	private final Set<String> apiSources = new HashSet<>();

	public Station(String name) {
		this(Entity.nameToId(name), name);
	}
	
	@ConstructorProperties({"id", "name"})
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
	
	public Adress getAdress() {
		return adress;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public StationServices getServices() {
		return services;
	}
	
	public Map<String, Object> getApiIds() {
		return apiIds;
	}
	
	public Set<String> getApiSources() {
		return apiSources;
	}

}
