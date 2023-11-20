package eu.derzauberer.pis.entity;

import org.apache.commons.lang3.StringUtils;

public interface NameEntity {

	String getName();
	
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
