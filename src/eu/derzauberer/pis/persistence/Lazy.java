package eu.derzauberer.pis.persistence;

import java.util.function.Function;
import java.util.function.Supplier;

public class Lazy<T> implements Comparable<Lazy<T>> {
	
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

	public <R> Lazy<R> map(Function<T, R> mapping) {
		return new Lazy<>(this.id, () -> mapping.apply(supplier.get()));
	}
	
	@Override
	public int compareTo(Lazy<T> lazy) {
		return this.getId().compareTo(lazy.getId());
	}
	
	

}
