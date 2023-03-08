package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "name", "platforms", "adress", "services"})
public class Station implements Entity<String> {
	
	private final String id;
	private final String name;
	private final ArrayList<Integer> platforms = new ArrayList<>();
	private final Adress adress = new Adress();
	private final StationServices services = new StationServices();

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
	
	public ArrayList<Integer> getPlatforms() {
		return platforms;
	}
	
	public Adress getAdress() {
		return adress;
	}
	
	public StationServices getServices() {
		return services;
	}

}
