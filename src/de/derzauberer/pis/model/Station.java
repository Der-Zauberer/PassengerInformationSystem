package de.derzauberer.pis.model;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "name"})
public class Station extends Entity<String> {

	@ConstructorProperties({"id", "name"})
	public Station(String id, String name) {
		super(id, name);
	}

}
