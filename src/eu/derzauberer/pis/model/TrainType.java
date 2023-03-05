package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "name", "token"})
public class TrainType implements Entity<String> {

	private final String id;
	private final String name;
	private final String token;
	
	public TrainType(String name, String token) {
		this(Entity.nameToId(name), name, token);
	}
	
	@ConstructorProperties({"id", "name", "token"})
	public TrainType(String id, String name, String token) {
		this.id = id;
		this.name = name;
		this.token = token;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public String getToken() {
		return token;
	}

}
