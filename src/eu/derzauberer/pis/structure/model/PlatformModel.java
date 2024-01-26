package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
	
	public String getName() {
		return name;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public Set<String> getLinkedPlatforms() {
		return linkedPlatforms;
	}

	@Override
	public int compareTo(PlatformModel platform) {
		return this.name.compareTo(platform.getName());
	}
	
}
