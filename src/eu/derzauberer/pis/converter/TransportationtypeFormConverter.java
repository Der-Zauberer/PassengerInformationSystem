package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.dto.TransportationTypeForm;
import eu.derzauberer.pis.model.ColorModel;
import eu.derzauberer.pis.model.TransportationTypeModel;

@Component
public class TransportationtypeFormConverter implements FormConverter<TransportationTypeModel, TransportationTypeForm> {

	@Override
	public TransportationTypeForm convertToForm(TransportationTypeModel type) {
		final TransportationTypeForm typeForm = new TransportationTypeForm();
		typeForm.setId(type.getId());
		typeForm.setName(type.getName());
		typeForm.setDescription(type.getDescription());
		typeForm.setVehicle(type.getVehicle());
		typeForm.setClassification(type.getClassification());
		typeForm.setColor(type.getColor());
		return typeForm;
	}

	@Override
	public TransportationTypeModel convertToModel(TransportationTypeForm typeForm) {
		final TransportationTypeModel type = new TransportationTypeModel(typeForm.getId(), typeForm.getName());
		return convertToModel(type, typeForm);
	}

	@Override
	public TransportationTypeModel convertToModel(TransportationTypeModel type, TransportationTypeForm typeForm) {
		type.setName(typeForm.getName());
		type.setDescription(typeForm.getDescription());
		type.setVehicle(typeForm.getVehicle());
		type.setClassification(typeForm.getClassification());
		if (typeForm.getColor() != null) type.setColor(new ColorModel(typeForm.getColor()));
		return type;
	}

}
