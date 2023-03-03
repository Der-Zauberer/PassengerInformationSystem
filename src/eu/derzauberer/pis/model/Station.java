package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "name", "platforms", "adress"})
public class Station extends Entity<String> {
	
	private final ArrayList<Integer> platforms = new ArrayList<>();
	private final Adress adress = new Adress();

	@ConstructorProperties({"id", "name"})
	public Station(String id, String name) {
		super(id, name);
	}
	
	public ArrayList<Integer> getPlatforms() {
		return platforms;
	}
	
	public Adress getAdress() {
		return adress;
	}

}
