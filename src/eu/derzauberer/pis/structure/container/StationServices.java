package eu.derzauberer.pis.structure.container;

public class StationServices {
	
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
	
	public StationServices() {}
	
	public StationServices(StationServices stationServices) {
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
	
	public boolean isParking() {
		return parking;
	}
	
	public void setParking(boolean parking) {
		this.parking = parking;
	}
	
	public boolean isBicycleParking() {
		return bicycleParking;
	}
	
	public void setBicycleParking(boolean bicycleParking) {
		this.bicycleParking = bicycleParking;
	}
	
	public boolean isLocalPublicTransport() {
		return localPublicTransport;
	}
	
	public void isLocalPublicTransport(boolean localPublicTransport) {
		this.localPublicTransport = localPublicTransport;
	}
	
	public boolean isPublicFacilities() {
		return publicFacilities;
	}
	
	public void setPublicFacilities(boolean publicFacilities) {
		this.publicFacilities = publicFacilities;
	}
	
	public boolean isTaxiRank() {
		return taxiRank;
	}
	
	public void setTaxiRank(boolean taxiRank) {
		this.taxiRank = taxiRank;
	}
	
	public boolean isCarRental() {
		return carRental;
	}
	
	public void setCarRental(boolean hasCarRental) {
		this.carRental = hasCarRental;
	}
	
	public boolean isLockerSystem() {
		return lockerSystem;
	}
	
	public void setLockerSystem(boolean lockerSystem) {
		this.lockerSystem = lockerSystem;
	}
	
	public boolean isTravelNecessities() {
		return travelNecessities;
	}
	
	public void setTravelNecessities(boolean travelNecessities) {
		this.travelNecessities = travelNecessities;
	}
	
	public boolean isBarrierFree() {
		return barrierFree;
	}
	
	public void setBarrierFree(boolean barrierFree) {
		this.barrierFree = barrierFree;
	}
	
	public boolean isWifi() {
		return wifi;
	}
	
	public void setWifi(boolean wifi) {
		this.wifi = wifi;
	}
	
	public boolean isTravelCenter() {
		return travelCenter;
	}
	
	public void setTravelCenter(boolean travelCenter) {
		this.travelCenter = travelCenter;
	}
	
	public boolean isRailwayMission() {
		return railwayMission;
	}
	
	public void setRailwayMission(boolean railwayMission) {
		this.railwayMission = railwayMission;
	}

}
