package eu.derzauberer.pis.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"parking", "bicycleParking", "localPublicTransport", "publicFacilities", "taxiRank", "hasCarRental", "lockerSystem", "travelNecessities", "barrierFree", "wifi", "travelCenter", "railwayMission"})
public class StationServices {
	
	private boolean parking = false;
	private boolean bicycleParking = false;
	private boolean localPublicTransport = false;
	private boolean publicFacilities = false;
	private boolean taxiRank = false;
	private boolean hasCarRental = false;
	private boolean lockerSystem = false;
	private boolean travelNecessities = false;
	private boolean barrierFree = false;
	private boolean wifi = false;
	private boolean travelCenter = false;
	private boolean railwayMission = false;
	
	public boolean hasParking() {
		return parking;
	}
	
	public void setParking(boolean parking) {
		this.parking = parking;
	}
	
	public boolean hasBicycleParking() {
		return bicycleParking;
	}
	
	public void setBicycleParking(boolean bicycleParking) {
		this.bicycleParking = bicycleParking;
	}
	
	public boolean hasLocalPublicTransport() {
		return localPublicTransport;
	}
	
	public void setLocalPublicTransport(boolean localPublicTransport) {
		this.localPublicTransport = localPublicTransport;
	}
	
	public boolean hasPublicFacilities() {
		return publicFacilities;
	}
	
	public void setPublicFacilities(boolean publicFacilities) {
		this.publicFacilities = publicFacilities;
	}
	
	public boolean hasTaxiRank() {
		return taxiRank;
	}
	
	public void setTaxiRank(boolean taxiRank) {
		this.taxiRank = taxiRank;
	}
	
	public boolean hasHasCarRental() {
		return hasCarRental;
	}
	
	public void setHasCarRental(boolean hasCarRental) {
		this.hasCarRental = hasCarRental;
	}
	
	public boolean hasLockerSystem() {
		return lockerSystem;
	}
	
	public void setLockerSystem(boolean lockerSystem) {
		this.lockerSystem = lockerSystem;
	}
	
	public boolean hasTravelNecessities() {
		return travelNecessities;
	}
	
	public void setTravelNecessities(boolean travelNecessities) {
		this.travelNecessities = travelNecessities;
	}
	
	public boolean hasBarrierFree() {
		return barrierFree;
	}
	
	public void setBarrierFree(boolean barrierFree) {
		this.barrierFree = barrierFree;
	}
	
	public boolean hasWifi() {
		return wifi;
	}
	
	public void setWifi(boolean wifi) {
		this.wifi = wifi;
	}
	
	public boolean hasTravelCenter() {
		return travelCenter;
	}
	
	public void setTravelCenter(boolean travelCenter) {
		this.travelCenter = travelCenter;
	}
	
	public boolean hasRailwayMission() {
		return railwayMission;
	}
	
	public void setRailwayMission(boolean railwayMission) {
		this.railwayMission = railwayMission;
	}

}
