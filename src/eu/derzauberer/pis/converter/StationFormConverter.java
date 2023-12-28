package eu.derzauberer.pis.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.Location;
import eu.derzauberer.pis.structure.container.Platform;
import eu.derzauberer.pis.structure.container.Services;
import eu.derzauberer.pis.structure.form.PlatformForm;
import eu.derzauberer.pis.structure.form.StationForm;
import eu.derzauberer.pis.structure.model.Station;

@Component
public class StationFormConverter implements FormConverter<Station, StationForm> {
	
	@Autowired
	private FormConverter<Platform, PlatformForm> platformFormConverter;

	@Override
	public StationForm convertToForm(Station station) {
		final StationForm stationForm = new StationForm();
		stationForm.setId(station.getId());
		stationForm.setName(station.getName());
		if (station.getPlatforms() != null) {
			stationForm.setPlatforms(station.getPlatforms().stream().map(platformFormConverter::convertToForm).toList());
		}
		station.getAddress().ifPresent(address -> stationForm.setAddress(new Address(address)));
		station.getLocation().ifPresent(location -> stationForm.setLocation(new Location(location)));
		station.getServices().ifPresent(services -> stationForm.setServices(new Services(services)));
		return stationForm;
	}

	@Override
	public Station convertToModel(StationForm stationForm) {
		final Station station = new Station(stationForm.getId(), stationForm.getName());
		return convertToModel(station, stationForm);
	}

	@Override
	public Station convertToModel(Station station, StationForm stationForm) {
		station.setName(stationForm.getName());
		if (stationForm.getPlatforms() != null) {
			stationForm.getPlatforms().stream().map(platformFormConverter::convertToModel).forEach(station.getPlatforms()::add);
		}
		if (stationForm.getAddress() != null) station.setAddress(new Address(stationForm.getAddress()));
		if (stationForm.getLocation() != null) station.setLocation(new Location(stationForm.getLocation()));
		if (stationForm.getServices() != null) station.setServices(new Services(stationForm.getServices()));
		return station;
	}

}
