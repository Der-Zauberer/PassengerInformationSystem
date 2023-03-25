package eu.derzauberer.pis.util;

public interface Entity<T extends Entity<T>> extends Comparable<T> {
	
	String getId();
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
	
	@Override
	default int compareTo(T o) {
		return getId().compareTo(o.getId());
	}
	
}
