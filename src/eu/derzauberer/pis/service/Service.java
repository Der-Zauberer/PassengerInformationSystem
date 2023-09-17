package eu.derzauberer.pis.service;

public abstract class Service {
	
	private final String name;
	
	public Service(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
