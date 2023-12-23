package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.container.Address;
import eu.derzauberer.pis.structure.container.ApiInformation;
import eu.derzauberer.pis.structure.container.Location;
import eu.derzauberer.pis.structure.container.Services;
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
		station.getAddress().ifPresent(address -> stationData.setAddress(new Address(address)));
		station.getLocation().ifPresent(location -> stationData.setLocation(new Location(location)));
		station.getServices().ifPresent(services -> stationData.setServices(new Services(services)));
		station.getApiInformation().ifPresent(api -> stationData.setApiInformation(new ApiInformation(api)));
		return stationData;
	}

}
