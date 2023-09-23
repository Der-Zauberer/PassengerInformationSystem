package eu.derzauberer.pis.enums;


public enum TransportationClassification {
	
	REGIONAL("entity.transportationClassification.regional"),
	LONG_DISTANCE("entity.transportationClassification.longDistance"),
	FREIGHT("entity.transportationClassification.freight");
	
	private final String localization;
	
	private TransportationClassification(String localization) {
		this.localization = localization;
	}
	
	public String getLocalization() {
		return localization;
	}

}
