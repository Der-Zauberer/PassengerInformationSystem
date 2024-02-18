package eu.derzauberer.pis.persistence;

import org.apache.commons.lang3.StringUtils;

public interface Namable extends Identifiable {

	String getName();
	
	default int compareSearchTo(String search, Namable namable) {
		final boolean station1startsWithName = getName().toLowerCase().startsWith(search.toLowerCase());
		final boolean station2startsWithName = namable.getName().toLowerCase().startsWith(search.toLowerCase());
		if (station1startsWithName && !station2startsWithName) return -1;
		if (!station1startsWithName && station2startsWithName) return 1;
		return 0;
	}
	
	static String nameToId(String name) {
		return StringUtils.stripAccents(name.toLowerCase()
				.replaceAll(" |\\/| \\/ |-", "_")
				.replaceAll("\\(|\\).", "")
				.replaceAll("ä", "ae")
				.replaceAll("ö", "oe")
				.replaceAll("ü", "ue")
				.replaceAll("ß", "ss"));
	}
	
}
