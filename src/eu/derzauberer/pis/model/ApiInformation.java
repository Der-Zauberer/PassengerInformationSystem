package eu.derzauberer.pis.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

public class ApiInformation {
	
	private Map<String, Object> ids;
	private Map<String, LocalDate> sources;
	
	public void addId(String name, Object id) {
		ids.put(name, id);
	}
	
	public Map<String, Object> getIds() {
		return Collections.unmodifiableMap(ids);
	}
	
	public void addSource(String path) {
		sources.put(path, LocalDate.now());
	}
	
	public Map<String, LocalDate> getSources() {
		return Collections.unmodifiableMap(sources);
	}

}
