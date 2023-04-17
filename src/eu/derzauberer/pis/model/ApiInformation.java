package eu.derzauberer.pis.model;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class ApiInformation {
	
	private Map<String, Object> ids;
	private Set<String> sources;
	private LocalDate lastUpdated = LocalDate.now();
	
	public Map<String, Object> getIds() {
		return ids;
	}
	
	public void setIds(Map<String, Object> ids) {
		this.ids = ids;
	}
	
	public Set<String> getSources() {
		return sources;
	}
	
	public void setSources(Set<String> sources) {
		this.sources = sources;
	}
	
	public LocalDate getLastUpdated() {
		return lastUpdated;
	}
	
	public void setLastUpdated(LocalDate lastTimeUpdated) {
		this.lastUpdated = lastTimeUpdated;
	}
	
	public void setLastUpdatedNow() {
		this.lastUpdated = LocalDate.now();
	}

}
