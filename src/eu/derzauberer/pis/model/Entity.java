package eu.derzauberer.pis.model;

public interface Entity<T> {
	
	T getId();
	String getName();
	
	static String nameToId(String name) {
		return name.toLowerCase()
				.replaceAll("/", "_")
				.replaceAll("\\(", "")
				.replaceAll("\\)", "")
				.replaceAll(" ", "_")
				.replaceAll("ä", "ae")
				.replaceAll("ö", "oe")
				.replaceAll("ü", "ue")
				.replaceAll("ß", "ue");
	}
	
}
