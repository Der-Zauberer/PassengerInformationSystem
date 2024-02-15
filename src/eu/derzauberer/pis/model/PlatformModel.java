package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.util.SortedSet;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlatformModel implements Comparable<PlatformModel> {
	
	private final String name;
	private int length;
	private final SortedSet<String> linkedPlatforms;
	
	@ConstructorProperties({ "name" })
	public PlatformModel(String name) {
		this.name = name;
		this.length = 0;
		this.linkedPlatforms = new TreeSet<>();
	}
	
	public PlatformModel(PlatformModel platform) {
		this.name = platform.name;
		this.length = platform.length;
		this.linkedPlatforms = new TreeSet<>(platform.linkedPlatforms);
	}

	@Override
	public int compareTo(PlatformModel platform) {
		return this.name.compareTo(platform.getName());
	}
	
}
