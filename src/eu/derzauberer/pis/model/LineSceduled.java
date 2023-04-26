package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import eu.derzauberer.pis.util.Entity;

@JsonPropertyOrder({ "id", "type", "number", "operator", "scedule", "stops", "api" })
public class LineSceduled extends Line<LineStop> implements Entity<LineSceduled> {
	
	private final LineScedule scedule = new LineScedule();

	@ConstructorProperties({ "id", "type", "number" })
	public LineSceduled(String id, TrainType type, int number) {
		super(id, type, number);
	}
	
	public LineScedule getScedule() {
		return scedule;
	}
	
}
