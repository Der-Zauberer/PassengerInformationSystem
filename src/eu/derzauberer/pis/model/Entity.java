package eu.derzauberer.pis.model;

public interface Entity<T> {
	
	T getId();
	String getName();
	
	static String nameToId(String name) {
		return name.toLowerCase()
				.replaceAll("(", "")
				.replace(")", "")
				.replace(" ", "_")
				.replace("ä", "ae")
				.replace("ö", "oe")
				.replace("ü", "ue");
	}
	
}
