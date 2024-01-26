package eu.derzauberer.pis.structure.model;

public class LocationModel {
	
	private double latitude;
	private double longitude;
	
	public LocationModel() {
		this(0, 0);
	}
	
	public LocationModel(LocationModel location) {
		this.latitude = location.latitude;
		this.longitude = location.longitude;
	}
	
	public LocationModel(double latitude, double longitude) {
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
