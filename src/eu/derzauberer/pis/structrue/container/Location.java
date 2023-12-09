package eu.derzauberer.pis.structrue.container;

public class Location {
	
	private double latitude;
	private double longitude;
	
	public Location() {
		this(0, 0);
	}
	
	public Location(Location location) {
		this.latitude = location.latitude;
		this.longitude = location.longitude;
	}
	
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
