package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Map;

public class SearchIndex {
	
	private final Map<String, String> originalNames;
	private final Map<String, List<String>> entries;
	
	@ConstructorProperties({ "originalNames", "entries" })
	public SearchIndex(Map<String, String> originalNames, Map<String, List<String>> entries) {
		this.originalNames = originalNames;
		this.entries = entries;
	}
	
	public Map<String, String> getOriginalNames() {
		return originalNames;
	}
	
	public Map<String, List<String>> getEntries() {
		return entries;
	}

}
