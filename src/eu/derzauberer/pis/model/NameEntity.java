package eu.derzauberer.pis.model;

public interface NameEntity {

	String getName();
	
	static String nameToId(String name) {
		return name.toLowerCase()
				.replaceAll(" |\\/| \\/ |-", "_")
				.replaceAll("\\(|\\)", "")
				.replaceAll("ä", "ae")
				.replaceAll("ö", "oe")
				.replaceAll("ü", "ue")
				.replaceAll("ß", "ss");
	}
	
}
