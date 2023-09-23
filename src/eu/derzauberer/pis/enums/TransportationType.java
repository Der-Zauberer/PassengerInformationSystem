package eu.derzauberer.pis.enums;


public enum TransportationType {
	
	BUS("entity.transportationType.bus"),
	TRAM("entity.transportationType.tram"),
	TRAIN("entity.transportationType.train"),
	PLANE("entity.transportationType.plane"),
	SHIP("entity.transportationType.ship");
	
	private final String localization;
	
	private TransportationType(String localization) {
		this.localization = localization;
	}
	
	public String getLocalization() {
		return localization;
	}

}
