package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

public class Location {
	
	private final double latitude;
	private final double longitude;
	
	@ConstructorProperties({ "latitude", "longitude" })
	public Location(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}

}
