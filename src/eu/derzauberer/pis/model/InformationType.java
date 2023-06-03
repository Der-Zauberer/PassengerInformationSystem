package eu.derzauberer.pis.model;


public enum InformationType {
	
	INFO(3),
	SUCCESS(2),
	WARNING(1),
	ERROR(0);
	
	private final int priority;

	InformationType(int priority) {
		this.priority = priority;
	}
	
	public int getPriority() {
		return priority;
	}

}
