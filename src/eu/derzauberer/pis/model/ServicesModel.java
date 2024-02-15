package eu.derzauberer.pis.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicesModel {
	
	private boolean parking = false;
	private boolean bicycleParking = false;
	private boolean localPublicTransport = false;
	private boolean publicFacilities = false;
	private boolean taxiRank = false;
	private boolean carRental = false;
	private boolean lockerSystem = false;
	private boolean travelNecessities = false;
	private boolean barrierFree = false;
	private boolean wifi = false;
	private boolean travelCenter = false;
	private boolean railwayMission = false;
	
	public ServicesModel() {}
	
	public ServicesModel(ServicesModel stationServices) {
		this.parking = stationServices.parking;
		this.bicycleParking = stationServices.bicycleParking;
		this.localPublicTransport = stationServices.localPublicTransport;
		this.publicFacilities = stationServices.publicFacilities;
		this.taxiRank = stationServices.taxiRank;
		this.carRental = stationServices.carRental;
		this.lockerSystem = stationServices.lockerSystem;
		this.travelNecessities = stationServices.travelNecessities;
		this.barrierFree = stationServices.barrierFree;
		this.wifi = stationServices.wifi;
		this.travelCenter = stationServices.travelCenter;
		this.railwayMission = stationServices.railwayMission;
	}

}
