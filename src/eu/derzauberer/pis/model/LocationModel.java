package eu.derzauberer.pis.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
