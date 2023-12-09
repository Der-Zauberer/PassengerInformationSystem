package eu.derzauberer.pis.structure.enums;

public enum TransportationVehicle {
	
	BUS("entity.transportationVehicle.bus"),
	TRAM("entity.transportationVehicle.tram"),
	TRAIN("entity.transportationVehicle.train"),
	PLANE("entity.transportationVehicle.plane"),
	SHIP("entity.transportationVehicle.ship");
	
	private final String localization;
	
	private TransportationVehicle(String localization) {
		this.localization = localization;
	}
	
	public String getLocalization() {
		return localization;
	}

}
