package eu.derzauberer.pis.dto;

import java.util.List;

import eu.derzauberer.pis.model.AddressModel;
import eu.derzauberer.pis.model.LocationModel;
import eu.derzauberer.pis.model.ServicesModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StationForm {
	
	private String id;
	private String name;
	private List<PlatformForm> platforms;
	private AddressModel address;
	private LocationModel location;
	private ServicesModel services;

}
