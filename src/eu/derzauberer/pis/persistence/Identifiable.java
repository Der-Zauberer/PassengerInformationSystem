package eu.derzauberer.pis.persistence;


public interface Identifiable {
	
	String getId();
	
	default String[] getIds() {
		return new String[] { getId() };
	}

}
