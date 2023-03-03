package de.derzauberer.pis.model;

import java.util.Objects;

public abstract class Entity<T> {
	
	private T id;
	private String name;
	
	public Entity(T id) {
		this.id = id;
		this.name = "Unnamed";
	}
	
	public Entity(T id, String name) {
		Objects.requireNonNull(id);
		this.id = id;
		this.name = name;
	}
	
	public T getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	

}
