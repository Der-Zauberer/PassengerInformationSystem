package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.dto.TransportationTypeData;
import eu.derzauberer.pis.structure.model.ApiInformationModel;
import eu.derzauberer.pis.structure.model.ColorModel;
import eu.derzauberer.pis.structure.model.TransportationTypeModel;

@Component
public class TransportationTypeDataConverter implements DataConverter<TransportationTypeModel, TransportationTypeData> {

	@Override
	public TransportationTypeData convert(TransportationTypeModel type) {
		final TransportationTypeData typeData = new TransportationTypeData();
		typeData.setId(type.getId());
		typeData.setName(type.getName());
		typeData.setDescription(type.getDescription());
		typeData.setVehicle(type.getVehicle());
		typeData.setClassification(type.getClassification());
		typeData.setColor(new ColorModel(type.getColor()));
		type.getApiInformation().ifPresent(api -> typeData.setApiInformation(new ApiInformationModel(api)));
		return typeData;
	}

}
