package eu.derzauberer.pis.structure.form;

public class PlatformForm {
	
	private String name;
	private int length;
	private String linkedPlatforms;
	
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
	
	public String getLinkedPlatforms() {
		return linkedPlatforms;
	}
	
	public void setLinkedPlatforms(String linkedPlattforms) {
		this.linkedPlatforms = linkedPlattforms;
	}

}
