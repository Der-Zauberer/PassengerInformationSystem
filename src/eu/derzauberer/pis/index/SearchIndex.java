package eu.derzauberer.pis.index;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Map;

public class SearchIndex {
	
	private final Map<String, List<String>> entries;
	
	@ConstructorProperties({ "entries" })
	public SearchIndex(Map<String, List<String>> entries) {
		this.entries = entries;
	}
	
	public Map<String, List<String>> getEntries() {
		return entries;
	}

}
