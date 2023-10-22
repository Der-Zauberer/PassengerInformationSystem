package eu.derzauberer.pis.util;

import java.util.List;
import java.util.function.Function;

public class CollectableMap<T, R> implements Collectable<R>{
	
	private Collectable<T> collectable;
	private Function<T, R> mapping;
	
	public CollectableMap(Collectable<T> collectable, Function<T, R> mapping) {
		this.collectable = collectable;
		this.mapping = mapping;
	}

	@Override
	public boolean isEmpty() {
		return collectable.isEmpty();
	}

	@Override
	public int size() {
		return collectable.size();
	}

	@Override
	public List<R> getAll() {
		return collectable.getAll().stream().map(mapping).toList();
	}

	@Override
	public List<R> getRange(int beginn, int end) {
		return collectable.getRange(beginn, end).stream().map(mapping).toList();
	}
	
	

}
