package eu.derzauberer.pis.util;

import java.util.List;

public class CollectableList<T> implements Collectable<T> {
	
	private final List<T> list;
	
	public CollectableList(List<T> list) {
		this.list = list;
	}
	
	@Override
	public int size() {
		return list.size();
	}
	
	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public List<T> getAll() {
		return list;
	}

	@Override
	public List<T> getRange(int beginn, int end) {
		return list.subList(beginn, end);
	}

}
