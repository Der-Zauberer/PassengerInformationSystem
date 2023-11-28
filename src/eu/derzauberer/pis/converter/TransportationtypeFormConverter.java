package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.form.TransportationTypeForm;
import eu.derzauberer.pis.model.TransportationType;

@Component
public class TransportationtypeFormConverter implements FormConverter<TransportationType, TransportationTypeForm> {

	@Override
	public TransportationTypeForm convertToForm(TransportationType type) {
		final TransportationTypeForm typeForm = new TransportationTypeForm();
		typeForm.setId(type.getId());
		typeForm.setName(type.getName());
		typeForm.setVehicle(type.getVehicle());
		typeForm.setClassification(type.getClassification());
		typeForm.setBackgroundColor(type.getBackgroundColor());
		typeForm.setTextColor(type.getTextColor());
		typeForm.setApi(type.getApiInformation());
		return typeForm;
	}

	@Override
	public TransportationType convertToModel(TransportationTypeForm typeForm) {
		final TransportationType type = new TransportationType(typeForm.getId(), typeForm.getName(), typeForm.getVehicle(), typeForm.getClassification());
		return convertToModel(type, typeForm);
	}

	@Override
	public TransportationType convertToModel(TransportationType type, TransportationTypeForm typeForm) {
		type.setBackgroundColor(typeForm.getBackgroundColor());
		type.setTextColor(typeForm.getTextColor());
		return type;
	}

}
