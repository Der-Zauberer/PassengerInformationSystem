package eu.derzauberer.pis.dto;

import java.util.SortedSet;

import eu.derzauberer.pis.model.AddressModel;
import eu.derzauberer.pis.model.ApiInformationModel;
import eu.derzauberer.pis.model.LocationModel;
import eu.derzauberer.pis.model.PlatformModel;
import eu.derzauberer.pis.model.ServicesModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StationData {
	
	private String id;
	private String name;
	private SortedSet<PlatformModel> platforms;
	private AddressModel address;
	private LocationModel location;
	private ServicesModel services;
	private ApiInformationModel apiInformation;

}
