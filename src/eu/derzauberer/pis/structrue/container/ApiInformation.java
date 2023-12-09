package eu.derzauberer.pis.structrue.container;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ApiInformation {
	
	private Map<String, Object> ids;
	private Map<String, LocalDate> sources;
	
	public ApiInformation() {}
	
	public ApiInformation(ApiInformation apiInformation) {
		if (apiInformation.ids != null) apiInformation.ids.forEach(ids::put);
		if (apiInformation.ids != null) apiInformation.sources.forEach(sources::put);
	}
	
	public void addId(String name, Object id) {
		if (ids == null) ids = new HashMap<>();
		ids.put(name, id);
	}
	
	public Map<String, Object> getIds() {
		return Collections.unmodifiableMap(ids);
	}
	
	public void addSource(String path) {
		if (sources == null) sources = new HashMap<>();
		sources.put(path, LocalDate.now());
	}
	
	public Map<String, LocalDate> getSources() {
		return Collections.unmodifiableMap(sources);
	}

}
