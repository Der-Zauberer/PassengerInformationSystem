package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.container.ApiInformation;
import eu.derzauberer.pis.structure.container.Color;
import eu.derzauberer.pis.structure.data.TransportationTypeData;
import eu.derzauberer.pis.structure.model.TransportationType;

@Component
public class TransportationTypeDataConverter implements DataConverter<TransportationType, TransportationTypeData> {

	@Override
	public TransportationTypeData convert(TransportationType type) {
		final TransportationTypeData typeData = new TransportationTypeData();
		typeData.setId(type.getId());
		typeData.setName(type.getName());
		typeData.setVehicle(type.getVehicle());
		typeData.setClassification(type.getClassification());
		type.getColor().ifPresent(color -> typeData.setColor(new Color(color)));
		type.getApiInformation().ifPresent(api -> typeData.setApiInformation(new ApiInformation(api)));
		return typeData;
	}

}
