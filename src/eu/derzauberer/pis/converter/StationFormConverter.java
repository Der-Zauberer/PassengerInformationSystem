package eu.derzauberer.pis.converter;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.Location;
import eu.derzauberer.pis.structure.container.Platform;
import eu.derzauberer.pis.structure.container.Services;
import eu.derzauberer.pis.structure.form.StationForm;
import eu.derzauberer.pis.structure.model.Station;

@Component
public class StationFormConverter implements FormConverter<Station, StationForm> {

	@Override
	public StationForm convertToForm(Station station) {
		final StationForm stationForm = new StationForm();
		stationForm.setId(station.getId());
		stationForm.setName(station.getName());
		if (station.getPlatforms() != null) stationForm.setPlatforms(copyPlatforms(station.getPlatforms()));
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
		if (stationForm.getPlatforms() != null) station.setPlatforms(copyPlatforms(stationForm.getPlatforms()));
		if (stationForm.getAddress() != null) station.setAddress(new Address(stationForm.getAddress()));
		if (stationForm.getLocation() != null) station.setLocation(new Location(stationForm.getLocation()));
		if (stationForm.getServices() != null) station.setServices(new Services(stationForm.getServices()));
		return station;
	}
	
	private SortedSet<Platform> copyPlatforms(SortedSet<Platform> platforms) {
		return platforms.stream().map(Platform::new).collect(Collectors.toCollection(TreeSet::new));
	}

}
