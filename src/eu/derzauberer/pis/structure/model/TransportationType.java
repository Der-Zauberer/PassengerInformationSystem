package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;
import java.util.Optional;

import eu.derzauberer.pis.structure.container.ApiInformation;
import eu.derzauberer.pis.structure.container.Color;
import eu.derzauberer.pis.structure.enums.TransportationClassification;
import eu.derzauberer.pis.structure.enums.TransportationVehicle;

public class TransportationType extends Entity<TransportationType> implements NameEntity {

	private final String id;
	private String name;
	private String description;
	private TransportationVehicle vehicle;
	private TransportationClassification classification;
	private Color color;
	private ApiInformation api;
	
	public TransportationType(String name) {
		this(NameEntity.nameToId(name), name);
	}
	
	@ConstructorProperties({ "id", "name" })
	public TransportationType(String id, String name) {
		this.id = id;
		this.name = name;
		this.vehicle = TransportationVehicle.TRAIN;
		this.classification = TransportationClassification.REGIONAL;
	}
	
	@Override
	public String getId() {
		return id;
	}
		
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description != null ? description : name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public TransportationVehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(TransportationVehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	public TransportationClassification getClassification() {
		return classification;
	}
	
	public void setClassification(TransportationClassification classification) {
		this.classification = classification;
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
		final TransportationType type = new TransportationType(this.id, this.name);
		type.description = this.description;
		type.vehicle = this.vehicle;
		type.classification = this.classification;
		if (color != null) type.color = new Color(this.color);
		if (api != null) type.api = new ApiInformation(this.api);
		return type;
	}

}
