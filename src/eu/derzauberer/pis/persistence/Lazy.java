package eu.derzauberer.pis.persistence;

import java.util.function.Supplier;

public class Lazy<T> {
	
	private final String id;
	private final Supplier<T> supplier;
	
	public Lazy(String id, Supplier<T> supplier) {
		this.id = id;
		this.supplier = supplier;
	}
	
	
	public String getId() {
		return id;
	}
	
	public T get() {
		return supplier.get();
	}

}
