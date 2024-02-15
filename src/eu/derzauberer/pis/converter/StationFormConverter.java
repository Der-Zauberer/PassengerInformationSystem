package eu.derzauberer.pis.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.derzauberer.pis.dto.PlatformForm;
import eu.derzauberer.pis.dto.StationForm;
import eu.derzauberer.pis.model.AddressModel;
import eu.derzauberer.pis.model.LocationModel;
import eu.derzauberer.pis.model.PlatformModel;
import eu.derzauberer.pis.model.ServicesModel;
import eu.derzauberer.pis.model.StationModel;

@Component
public class StationFormConverter implements FormConverter<StationModel, StationForm> {
	
	@Autowired
	private FormConverter<PlatformModel, PlatformForm> platformFormConverter;

	@Override
	public StationForm convertToForm(StationModel station) {
		final StationForm stationForm = new StationForm();
		stationForm.setId(station.getId());
		stationForm.setName(station.getName());
		stationForm.setPlatforms(station.getPlatforms().stream().map(platformFormConverter::convertToForm).toList());
		station.getAddress().ifPresent(address -> stationForm.setAddress(new AddressModel(address)));
		station.getLocation().ifPresent(location -> stationForm.setLocation(new LocationModel(location)));
		station.getServices().ifPresent(services -> stationForm.setServices(new ServicesModel(services)));
		return stationForm;
	}

	@Override
	public StationModel convertToModel(StationForm stationForm) {
		final StationModel station = new StationModel(stationForm.getId(), stationForm.getName());
		return convertToModel(station, stationForm);
	}

	@Override
	public StationModel convertToModel(StationModel station, StationForm stationForm) {
		station.setName(stationForm.getName());
		if (stationForm.getPlatforms() != null) {
			station.getPlatforms().clear();
			stationForm.getPlatforms().stream().map(platformFormConverter::convertToModel).forEach(station.getPlatforms()::add);
		}
		if (stationForm.getAddress() != null) station.setAddress(new AddressModel(stationForm.getAddress()));
		if (stationForm.getLocation() != null) station.setLocation(new LocationModel(stationForm.getLocation()));
		if (stationForm.getServices() != null) station.setServices(new ServicesModel(stationForm.getServices()));
		return station;
	}

}
