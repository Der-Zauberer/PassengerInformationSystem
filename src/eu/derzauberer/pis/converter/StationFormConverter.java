package eu.derzauberer.pis.converter;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.ApiInformation;
import eu.derzauberer.pis.structure.container.Location;
import eu.derzauberer.pis.structure.container.Platform;
import eu.derzauberer.pis.structure.container.StationServices;
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
		if (station.getAddress() != null) stationForm.setAddress(new Address(station.getAddress()));
		if (station.getLocation() != null) stationForm.setLocation(new Location(station.getLocation()));
		if (station.getServices() != null) stationForm.setServices(new StationServices(station.getServices()));
		if (station.getApiInformation() != null) stationForm.setApiInformation(new ApiInformation(station.getApiInformation()));
		return stationForm;
	}

	@Override
	public Station convertToModel(StationForm stationForm) {
		final Station station = new Station(stationForm.getId(), stationForm.getName());
		return convertToModel(station, stationForm);
	}

	@Override
	public Station convertToModel(Station station, StationForm stationForm) {
		if (stationForm.getPlatforms() != null) station.setPlatforms(copyPlatforms(stationForm.getPlatforms()));
		if (stationForm.getAddress() != null) station.setAddress(new Address(stationForm.getAddress()));
		if (stationForm.getLocation() != null) station.setLocation(new Location(stationForm.getLocation()));
		if (stationForm.getServices() != null) station.setServices(new StationServices(stationForm.getServices()));
		if (stationForm.getApiInformation() != null) station.setApiInformation(new ApiInformation(stationForm.getApiInformation()));
		return station;
	}
	
	private SortedSet<Platform> copyPlatforms(SortedSet<Platform> platforms) {
		return platforms.stream().map(Platform::new).collect(Collectors.toCollection(TreeSet::new));
	}

}
