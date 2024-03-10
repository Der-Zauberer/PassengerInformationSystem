package eu.derzauberer.pis.dto;

import eu.derzauberer.pis.model.AddressModel;
import eu.derzauberer.pis.model.ColorModel;
import eu.derzauberer.pis.model.OperatorModel;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OperatorForm {
	
	@NotBlank
	private String id;
	
	@NotBlank
	private String name;
	
	private AddressModel address;
	
	private ColorModel color;
	
	public OperatorForm(OperatorModel operator) {
		id = operator.getId();
		name = operator.getName();
		operator.getAddress().ifPresent(address -> address = new AddressModel(address));
		color = new ColorModel(operator.getColor());
	}
	
	public OperatorModel toOperatorModel() {
		return toOperatorModel(new OperatorModel(id, name));
	}
	
	public OperatorModel toOperatorModel(OperatorModel existingOperator) {
		existingOperator.setName(name);
		if (address != null) existingOperator.setAddress(new AddressModel(address));
		if (color != null) existingOperator.setColor(new ColorModel(color));
		return existingOperator;
	}
	
}
