package eu.derzauberer.pis.dto;

import eu.derzauberer.pis.model.AddressModel;
import eu.derzauberer.pis.model.ColorModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperatorForm {
	
	private String id;
	private String name;
	private AddressModel address;
	private ColorModel color;

}
