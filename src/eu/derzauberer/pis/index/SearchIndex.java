package eu.derzauberer.pis.index;

import java.beans.ConstructorProperties;
import java.util.Map;
import java.util.Set;

public class SearchIndex {
	
	private final Map<String, Set<String>> entries;
	
	@ConstructorProperties({ "entries" })
	public SearchIndex(Map<String, Set<String>> entries) {
		this.entries = entries;
	}
	
	public Map<String, Set<String>> getEntries() {
		return entries;
	}

}
