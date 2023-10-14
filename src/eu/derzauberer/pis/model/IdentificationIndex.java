package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.util.Map;

public class IdentificationIndex {
	
	private Map<String,  String> entries;
	
	@ConstructorProperties({ "entries" })
	public IdentificationIndex(Map<String,  String> entries) {
		this.entries = entries;
	}
	
	public Map<String, String> getEntries() {
		return entries;
	}

}
