package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.ApiInformation;
import eu.derzauberer.pis.structure.data.OperatorData;
import eu.derzauberer.pis.structure.model.Operator;

@Component
public class OperatorDataConverter implements DataConverter<Operator, OperatorData> {

	@Override
	public OperatorData convert(Operator operator) {
		final OperatorData operatorData = new OperatorData();
		operatorData.setId(operator.getId());
		operatorData.setName(operator.getName());
		if (operator.getAddress() != null) operatorData.setAddress(new Address(operator.getAddress()));
		operatorData.setBackgroundColor(operator.getBackgroundColor());
		operatorData.setTextColor(operator.getTextColor());
		if (operator.getApiInformation() != null) operatorData.setApiInformation(new ApiInformation(operator.getApiInformation()));
		return operatorData;
	}

}
