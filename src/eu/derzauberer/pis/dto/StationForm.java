package eu.derzauberer.pis.dto;

import java.util.List;

import eu.derzauberer.pis.model.AddressModel;
import eu.derzauberer.pis.model.LocationModel;
import eu.derzauberer.pis.model.ServicesModel;
import eu.derzauberer.pis.model.StationModel;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StationForm {
	
	@NotBlank
	private String id;
	
	@NotBlank
	private String name;
	
	private List<PlatformForm> platforms;
	
	private AddressModel address;
	
	private LocationModel location;
	
	private ServicesModel services;
	
	public StationForm(StationModel station) {
		id = station.getId();
		name = station.getName();
		platforms = station.getPlatforms().stream().map(PlatformForm::new).toList();
		station.getAddress().ifPresent(address -> this.address = new AddressModel(address));
		station.getLocation().ifPresent(location -> this.location = new LocationModel(location));
		station.getServices().ifPresent(services -> this.services = new ServicesModel(services));
	}
	
	public StationModel toStationModel() {
		return toStationModel(new StationModel(id, name));
	}
	
	public StationModel toStationModel(StationModel existingStation) {
		existingStation.setName(name);
		if (platforms != null) {
			existingStation.getPlatforms().clear();
			platforms.stream().map(PlatformForm::toPlatformModel).forEach(existingStation.getPlatforms()::add);
		}
		if (address != null) existingStation.setAddress(new AddressModel(address));
		if (location != null) existingStation.setLocation(new LocationModel(location));
		if (services != null) existingStation.setServices(new ServicesModel(services));
		return existingStation;
	}

}
