package eu.derzauberer.pis.enums;

public enum TransportationVehicle {
	
	BUS("entity.transportationType.bus"),
	TRAM("entity.transportationType.tram"),
	TRAIN("entity.transportationType.train"),
	PLANE("entity.transportationType.plane"),
	SHIP("entity.transportationType.ship");
	
	private final String localization;
	
	private TransportationVehicle(String localization) {
		this.localization = localization;
	}
	
	public String getLocalization() {
		return localization;
	}

}
