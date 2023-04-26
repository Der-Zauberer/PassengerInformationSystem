package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.util.Set;
import java.util.TreeSet;

public class Platform implements Comparable<Platform> {
	
	private final String name;
	private Integer length;
	private final TreeSet<String> linkedPlattforms = new TreeSet<>();
	
	@ConstructorProperties({ "name" })
	public Platform(String name) {
		this.name = name;
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
	
	public Set<String> getLinkedPlattforms() {
		return linkedPlattforms;
	}

	@Override
	public int compareTo(Platform o) {
		return this.name.compareTo(o.getName());
	}
	
}
