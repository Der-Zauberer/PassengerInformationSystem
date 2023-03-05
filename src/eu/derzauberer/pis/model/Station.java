package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "name", "platforms", "adress"})
public class Station implements Entity<String> {
	
	private final String id;
	private final String name;
	private final ArrayList<Integer> platforms = new ArrayList<>();
	private final Adress adress = new Adress();

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

}
