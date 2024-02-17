package eu.derzauberer.pis.persistence;

import java.util.Set;

public interface Identifiable {
	
	String getId();
	
	default Set<String> getSecondaryIds() {
		return Set.of();
	}

}
