package eu.derzauberer.pis.dto;

import eu.derzauberer.pis.enums.TransportationClassification;
import eu.derzauberer.pis.enums.TransportationVehicle;
import eu.derzauberer.pis.model.ColorModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportationTypeForm {
	
	private String id;
	private String name;
	private String description;
	private TransportationVehicle vehicle = TransportationVehicle.TRAIN;
	private TransportationClassification classification = TransportationClassification.REGIONAL;
	private ColorModel color;

}
