package eu.derzauberer.pis.dto;

import java.util.ArrayList;
import java.util.List;

public class ListDto<T> {
	
	private final int offset;
    private final int limit;
    private final int total;
    private final ArrayList<T> results;
	
	public ListDto(int offset, int limit, List<T> list) {
		this.offset = offset;
		this.limit = limit >= 0 ? limit : list.size();
		this.total = list.size();
		this.results = new ArrayList<>();
		final int max = this.offset + this.limit;
		for (int i = this.offset; i < max && i < list.size(); i++) {
			this.results.add(list.get(i));
		}
	}
	
	public int getOffset() {
		return offset;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public int getTotal() {
		return total;
	}
	
	public ArrayList<T> getResults() {
		return results;
	}
	
}
