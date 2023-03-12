package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

public class Location {
	
	private double latitude;
	private double longitude;
	
	@ConstructorProperties({"latitude", "longitude"})
	public Location(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
