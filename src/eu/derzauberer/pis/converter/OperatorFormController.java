package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.dto.OperatorForm;
import eu.derzauberer.pis.model.AddressModel;
import eu.derzauberer.pis.model.ColorModel;
import eu.derzauberer.pis.model.OperatorModel;

@Component
public class OperatorFormController implements FormConverter<OperatorModel, OperatorForm>{

	@Override
	public OperatorForm convertToForm(OperatorModel operator) {
		final OperatorForm operatorForm = new OperatorForm();
		operatorForm.setId(operator.getId());
		operatorForm.setName(operator.getName());
		operator.getAddress().ifPresent(address -> operatorForm.setAddress(new AddressModel(address)));
		operatorForm.setColor(new ColorModel(operator.getColor()));
		return operatorForm;
	}

	@Override
	public OperatorModel convertToModel(OperatorForm operatorForm) {
		final OperatorModel operator = new OperatorModel(operatorForm.getId(), operatorForm.getName());
		return convertToModel(operator, operatorForm);
	}

	@Override
	public OperatorModel convertToModel(OperatorModel operator, OperatorForm operatorForm) {
		operator.setName(operatorForm.getName());
		if (operatorForm.getAddress() != null) operator.setAddress(new AddressModel(operatorForm.getAddress()));
		if (operatorForm.getColor() != null) operator.setColor(new ColorModel(operatorForm.getColor()));
		return operator;
	}

}
