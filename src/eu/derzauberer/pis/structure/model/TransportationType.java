package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;
import java.util.Optional;

import eu.derzauberer.pis.structure.container.ApiInformation;
import eu.derzauberer.pis.structure.container.Color;
import eu.derzauberer.pis.structure.enums.TransportationClassification;
import eu.derzauberer.pis.structure.enums.TransportationVehicle;

public class TransportationType extends Entity<TransportationType> implements NameEntity {

	private final String id;
	private final String name;
	private final TransportationVehicle vehicle;
	private final TransportationClassification classification;
	private Color color;
	private ApiInformation api;
	
	@ConstructorProperties({ "id", "name", "vehicle", "classification" })
	public TransportationType(String id, String name, TransportationVehicle vehicle, TransportationClassification classification) {
		this.id = id;
		this.name = name;
		this.vehicle = vehicle;
		this.classification = classification;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	
	public TransportationVehicle getVehicle() {
		return vehicle;
	}

	public TransportationClassification getClassification() {
		return classification;
	}
	
	public Optional<Color> getColor() {
		return Optional.ofNullable(color);
	}
	
	public Color getOrCreateColor() {
		if (color == null) color = new Color();
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Optional<ApiInformation> getApiInformation() {
		return Optional.ofNullable(api);
	}
	
	public ApiInformation getOrCreateApiInformation() {
		if (api == null) api = new ApiInformation();
		return api;
	}
	
	public void setApiInformation(ApiInformation api) {
		this.api = api;
	}
	
	@Override
	public TransportationType copy() {
		final TransportationType type = new TransportationType(this.id, this.name, this.vehicle, this.classification);
		if (color != null) type.color = new Color(this.color);
		if (api != null) type.api = new ApiInformation(this.api);
		return type;
	}

}
