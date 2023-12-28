package eu.derzauberer.pis.structure.container;

import java.beans.ConstructorProperties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Platform implements Comparable<Platform> {
	
	private final String name;
	private int length;
	private final SortedSet<String> linkedPlatforms;
	
	@ConstructorProperties({ "name" })
	public Platform(String name) {
		this.name = name;
		this.length = 0;
		this.linkedPlatforms = new TreeSet<>();
	}
	
	public Platform(Platform platform) {
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
	public int compareTo(Platform platform) {
		return this.name.compareTo(platform.getName());
	}
	
}
