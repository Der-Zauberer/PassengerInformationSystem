package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.ApiInformation;
import eu.derzauberer.pis.structure.container.Color;
import eu.derzauberer.pis.structure.data.OperatorData;
import eu.derzauberer.pis.structure.model.Operator;

@Component
public class OperatorDataConverter implements DataConverter<Operator, OperatorData> {

	@Override
	public OperatorData convert(Operator operator) {
		final OperatorData operatorData = new OperatorData();
		operatorData.setId(operator.getId());
		operatorData.setName(operator.getName());
		operator.getAddress().ifPresent(address -> operatorData.setAddress(new Address(address)));
		operatorData.setColor(new Color(operator.getColor()));
		operator.getApiInformation().ifPresent(api -> operatorData.setApiInformation(new ApiInformation(api)));
		return operatorData;
	}

}
