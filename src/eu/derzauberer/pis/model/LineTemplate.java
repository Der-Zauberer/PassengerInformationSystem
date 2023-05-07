package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "type", "number", "operatorId", "stops", "api" })
public class LineTemplate extends Line<LineStop> implements Entity<LineTemplate> {

	@ConstructorProperties({ "id", "type", "number" })
	public LineTemplate(String id, TrainType type, int number) {
		super(id, type, number);
	}

}
