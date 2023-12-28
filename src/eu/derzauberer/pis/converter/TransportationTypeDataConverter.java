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
		typeData.setDescription(type.getDescription());
		typeData.setVehicle(type.getVehicle());
		typeData.setClassification(type.getClassification());
		typeData.setColor(new Color(type.getColor()));
		type.getApiInformation().ifPresent(api -> typeData.setApiInformation(new ApiInformation(api)));
		return typeData;
	}

}
