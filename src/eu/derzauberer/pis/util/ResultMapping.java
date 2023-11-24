package eu.derzauberer.pis.util;

import java.util.List;
import java.util.function.Function;

public class ResultMapping<T, R> implements Result<R>{
	
	private Result<T> collectable;
	private Function<T, R> mapping;
	
	public ResultMapping(Result<T> collectable, Function<T, R> mapping) {
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
