package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.Color;
import eu.derzauberer.pis.structure.form.OperatorForm;
import eu.derzauberer.pis.structure.model.Operator;

@Component
public class OperatorFormController implements FormConverter<Operator, OperatorForm>{

	@Override
	public OperatorForm convertToForm(Operator operator) {
		final OperatorForm operatorForm = new OperatorForm();
		operatorForm.setId(operator.getId());
		operatorForm.setName(operator.getName());
		operator.getAddress().ifPresent(address -> operatorForm.setAddress(new Address(address)));
		operatorForm.setColor(new Color(operator.getColor()));
		return operatorForm;
	}

	@Override
	public Operator convertToModel(OperatorForm operatorForm) {
		final Operator operator = new Operator(operatorForm.getId(), operatorForm.getName());
		return convertToModel(operator, operatorForm);
	}

	@Override
	public Operator convertToModel(Operator operator, OperatorForm operatorForm) {
		operator.setName(operatorForm.getName());
		if (operatorForm.getAddress() != null) operator.setAddress(new Address(operatorForm.getAddress()));
		if (operatorForm.getColor() != null) operator.setColor(new Color(operatorForm.getColor()));
		return operator;
	}

}
