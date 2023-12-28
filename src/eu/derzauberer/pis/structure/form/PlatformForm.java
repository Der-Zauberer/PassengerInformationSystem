package eu.derzauberer.pis.structure.form;

import java.util.List;

public class PlatformForm {
	
	private String name;
	private int length;
	private List<String> linkedPlatforms;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public List<String> getLinkedPlatforms() {
		return linkedPlatforms;
	}
	
	public void setLinkedPlatforms(List<String> linkedPlattforms) {
		this.linkedPlatforms = linkedPlattforms;
	}

}
