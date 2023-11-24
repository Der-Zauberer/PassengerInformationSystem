package eu.derzauberer.pis.model;

public interface Entity<T extends Entity<T>> extends Comparable<T> {
	
	String getId();
	
	@Override
	default int compareTo(T o) {
		return getId().compareTo(o.getId());
	}
	
}
