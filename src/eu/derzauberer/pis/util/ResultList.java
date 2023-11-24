package eu.derzauberer.pis.util;

import java.util.List;

public class ResultList<T> implements Result<T> {
	
	private final List<T> list;
	
	public ResultList(List<T> list) {
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
