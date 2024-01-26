package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.dto.OperatorData;
import eu.derzauberer.pis.structure.model.AddressModel;
import eu.derzauberer.pis.structure.model.ApiInformationModel;
import eu.derzauberer.pis.structure.model.ColorModel;
import eu.derzauberer.pis.structure.model.OperatorModel;

@Component
public class OperatorDataConverter implements DataConverter<OperatorModel, OperatorData> {

	@Override
	public OperatorData convert(OperatorModel operator) {
		final OperatorData operatorData = new OperatorData();
		operatorData.setId(operator.getId());
		operatorData.setName(operator.getName());
		operator.getAddress().ifPresent(address -> operatorData.setAddress(new AddressModel(address)));
		operatorData.setColor(new ColorModel(operator.getColor()));
		operator.getApiInformation().ifPresent(api -> operatorData.setApiInformation(new ApiInformationModel(api)));
		return operatorData;
	}

}
