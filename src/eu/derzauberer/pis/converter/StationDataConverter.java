package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.ApiInformation;
import eu.derzauberer.pis.structure.container.Location;
import eu.derzauberer.pis.structure.container.StationServices;
import eu.derzauberer.pis.structure.data.StationData;
import eu.derzauberer.pis.structure.model.Station;

@Component
public class StationDataConverter implements DataConverter<Station, StationData> {

	@Override
	public StationData convert(Station station) {
		final StationData stationData = new StationData();
		stationData.setId(station.getId());
		stationData.setName(station.getName());
		if (station.getPlatforms() != null) stationData.setPlatforms(stationData.getPlatforms());
		if (station.getAddress() != null) stationData.setAddress(new Address(station.getAddress()));
		if (station.getLocation() != null) stationData.setLocation(new Location(station.getLocation()));
		if (station.getServices() != null) stationData.setServices(new StationServices(station.getServices()));
		if (station.getApiInformation() != null) stationData.setApiInformation(new ApiInformation(station.getApiInformation()));
		return stationData;
	}

}
