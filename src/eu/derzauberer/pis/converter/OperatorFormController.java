package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.form.OperatorForm;
import eu.derzauberer.pis.structure.model.Operator;

@Component
public class OperatorFormController implements FormConverter<Operator, OperatorForm>{

	@Override
	public OperatorForm convertToForm(Operator operator) {
		final OperatorForm operatorForm = new OperatorForm();
		operatorForm.setId(operator.getId());
		operatorForm.setName(operator.getName());
		if (operator.getAddress() != null) operatorForm.setAddress(new Address(operator.getAddress()));
		operatorForm.setBackgorundColor(operator.getBackgorundColor());
		operatorForm.setTextColor(operator.getTextColor());
		return operatorForm;
	}

	@Override
	public Operator convertToModel(OperatorForm operatorForm) {
		return new Operator(operatorForm.getId(), operatorForm.getName());
	}

	@Override
	public Operator convertToModel(Operator operator, OperatorForm operatorForm) {
		if (operatorForm.getAddress() != null) operator.setAddress(new Address(operatorForm.getAddress()));
		operator.setBackgorundColor(operatorForm.getBackgorundColor());
		operator.setTextColor(operatorForm.getTextColor());
		return operator;
	}

}
