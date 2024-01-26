package eu.derzauberer.pis.converter;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.dto.StationData;
import eu.derzauberer.pis.structure.model.AddressModel;
import eu.derzauberer.pis.structure.model.ApiInformationModel;
import eu.derzauberer.pis.structure.model.LocationModel;
import eu.derzauberer.pis.structure.model.ServicesModel;
import eu.derzauberer.pis.structure.model.StationModel;

@Component
public class StationDataConverter implements DataConverter<StationModel, StationData> {

	@Override
	public StationData convert(StationModel station) {
		final StationData stationData = new StationData();
		stationData.setId(station.getId());
		stationData.setName(station.getName());
		if (station.getPlatforms() != null) stationData.setPlatforms(station.getPlatforms());
		station.getAddress().ifPresent(address -> stationData.setAddress(new AddressModel(address)));
		station.getLocation().ifPresent(location -> stationData.setLocation(new LocationModel(location)));
		station.getServices().ifPresent(services -> stationData.setServices(new ServicesModel(services)));
		station.getApiInformation().ifPresent(api -> stationData.setApiInformation(new ApiInformationModel(api)));
		return stationData;
	}

}
