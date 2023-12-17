package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.container.Color;
import eu.derzauberer.pis.structure.form.TransportationTypeForm;
import eu.derzauberer.pis.structure.model.TransportationType;

@Component
public class TransportationtypeFormConverter implements FormConverter<TransportationType, TransportationTypeForm> {

	@Override
	public TransportationTypeForm convertToForm(TransportationType type) {
		final TransportationTypeForm typeForm = new TransportationTypeForm();
		typeForm.setId(type.getId());
		typeForm.setName(type.getName());
		typeForm.setDescription(type.getDescription());
		typeForm.setVehicle(type.getVehicle());
		typeForm.setClassification(type.getClassification());
		type.getColor().ifPresent(color -> typeForm.setColor(new Color(color)));
		return typeForm;
	}

	@Override
	public TransportationType convertToModel(TransportationTypeForm typeForm) {
		final TransportationType type = new TransportationType(typeForm.getId(), typeForm.getName());
		return convertToModel(type, typeForm);
	}

	@Override
	public TransportationType convertToModel(TransportationType type, TransportationTypeForm typeForm) {
		type.setName(typeForm.getName());
		type.setDescription(typeForm.getDescription());
		type.setVehicle(typeForm.getVehicle());
		type.setClassification(typeForm.getClassification());
		if (typeForm.getColor() != null) type.setColor(new Color(typeForm.getColor()));
		return type;
	}

}
