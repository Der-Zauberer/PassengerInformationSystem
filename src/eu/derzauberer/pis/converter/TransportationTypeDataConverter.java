package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.data.TransportationTypeData;
import eu.derzauberer.pis.model.TransportationType;

@Component
public class TransportationTypeDataConverter implements DataConverter<TransportationType, TransportationTypeData> {

	@Override
	public TransportationTypeData convert(TransportationType type) {
		final TransportationTypeData typeData = new TransportationTypeData();
		typeData.setId(type.getId());
		typeData.setName(type.getName());
		typeData.setVehicle(type.getVehicle());
		typeData.setClassification(type.getClassification());
		typeData.setBackgroundColor(type.getBackgroundColor());
		typeData.setTextColor(type.getTextColor());
		typeData.setApi(type.getApiInformation());
		return typeData;
	}

}
