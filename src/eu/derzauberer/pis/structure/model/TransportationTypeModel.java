package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;
import java.util.Objects;
import java.util.Optional;

import eu.derzauberer.pis.structure.enums.TransportationClassification;
import eu.derzauberer.pis.structure.enums.TransportationVehicle;

public class TransportationTypeModel extends EntityModel<TransportationTypeModel> implements NameEntityModel {

	private final String id;
	private String name;
	private String description;
	private TransportationVehicle vehicle;
	private TransportationClassification classification;
	private ColorModel color;
	private ApiInformationModel api;
	
	public TransportationTypeModel(String name) {
		this(NameEntityModel.nameToId(name), name);
	}
	
	@ConstructorProperties({ "id", "name" })
	public TransportationTypeModel(String id, String name) {
		this.id = id;
		this.name = name;
		this.vehicle = TransportationVehicle.TRAIN;
		this.classification = TransportationClassification.REGIONAL;
		this.color = new ColorModel();
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
	
	public ColorModel getColor() {
		return color;
	}
	
	public void setColor(ColorModel color) {
		Objects.nonNull(color);
		this.color = color;
	}
	
	public Optional<ApiInformationModel> getApiInformation() {
		return Optional.ofNullable(api);
	}
	
	public ApiInformationModel getOrCreateApiInformation() {
		if (api == null) api = new ApiInformationModel();
		return api;
	}
	
	public void setApiInformation(ApiInformationModel api) {
		this.api = api;
	}
	
	@Override
	public TransportationTypeModel copy() {
		final TransportationTypeModel type = new TransportationTypeModel(this.id, this.name);
		type.description = this.description;
		type.vehicle = this.vehicle;
		type.classification = this.classification;
		type.color = new ColorModel(this.color);
		if (api != null) type.api = new ApiInformationModel(this.api);
		return type;
	}

}
