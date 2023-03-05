package eu.derzauberer.pis.model;

public class Line implements Entity<Long>{

	private final Long id;
	private String type;
	private int number;
	private String operator;
	private String driver;
	private boolean cancelled;
	
	public Line(Long id, String type, int number) {
		this.id = id;
		this.type = type;
		this.number = number;
	}
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return type + number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}