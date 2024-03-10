package eu.derzauberer.pis.dto;

import eu.derzauberer.pis.enums.TransportationClassification;
import eu.derzauberer.pis.enums.TransportationVehicle;
import eu.derzauberer.pis.model.ColorModel;
import eu.derzauberer.pis.model.TransportationTypeModel;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransportationTypeForm {
	
	@NotBlank
	private String id;
	
	@NotBlank
	private String name;
	
	@Nullable
	private String description;
	
	private TransportationVehicle vehicle = TransportationVehicle.TRAIN;
	
	private TransportationClassification classification = TransportationClassification.REGIONAL;
	
	private ColorModel color;
	
	public TransportationTypeForm(TransportationTypeModel type) {
		id = type.getId();
		name = type.getName();
		description = type.getDescription();
		vehicle = type.getVehicle();
		classification = type.getClassification();
		color = type.getColor();
	}
	
	public TransportationTypeModel toTransportationTypeModel() {
		return toTransportationTypeModel(new TransportationTypeModel(id, name));
	}
	
	public TransportationTypeModel toTransportationTypeModel(TransportationTypeModel existingType) {
		existingType.setName(name);
		existingType.setDescription(description);
		existingType.setVehicle(vehicle);
		existingType.setClassification(classification);
		if (color != null) existingType.setColor(color);
		return existingType;
	}

}
