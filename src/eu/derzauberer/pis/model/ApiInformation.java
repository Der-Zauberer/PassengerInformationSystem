package eu.derzauberer.pis.model;

import java.time.LocalDate;
import java.util.Map;

public class ApiInformation {
	
	private Map<String, Object> ids;
	private Map<String, LocalDate> sources;
	
	public Map<String, Object> getIds() {
		return ids;
	}
	
	public void setIds(Map<String, Object> ids) {
		this.ids = ids;
	}
	
	public Map<String, LocalDate> getSources() {
		return sources;
	}
	
	public void setSources(Map<String, LocalDate> sources) {
		this.sources = sources;
	}

}
