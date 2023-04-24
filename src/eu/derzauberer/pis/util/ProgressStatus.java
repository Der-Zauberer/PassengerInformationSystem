package eu.derzauberer.pis.util;


public class ProgressStatus {
	
	private final String name;
	private final int total;
	private final long section;
	private int percent;
	private int counter;
	
	public ProgressStatus(String name, int total) {
		this.name = name;
		this.total = total;
		this.section = total / 100;
		this.percent = 0;
		this.counter = 0;
	}
	
	public void count() {
		if (total < 200 || counter++ <= section - 1) return;
		counter = 0;
		System.out.print("Loading " + name + ": " + ++percent + "%\r");
	}
	
	public void count(String entity) {
		if (total < 200 || counter++ <= section - 1) {
			System.out.print("Loading " + name + ": " + percent + "% " + entity + "\r");
			return;
		}
		counter = 0;
		System.out.print("Loading " + name + ": " + ++percent + "% " + entity + "\r");
	}

}
