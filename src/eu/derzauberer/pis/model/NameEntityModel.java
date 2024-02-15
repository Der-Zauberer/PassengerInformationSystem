package eu.derzauberer.pis.model;

import org.apache.commons.lang3.StringUtils;

public interface NameEntityModel {

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
