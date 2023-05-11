package eu.derzauberer.pis.dto;

import java.util.ArrayList;
import java.util.List;

public class ListDto<T> {
	
	private final int offset;
    private final int limit;
    private int total;
    private final ArrayList<T> results;
    
    public ListDto(List<T> list) {
    	this(list, list.size(), 0);
    }
    
    public ListDto(List<T> list, int limit) {
    	this(list, limit, 0);
    }
	
	public ListDto(List<T> list, int limit, int offset) {
		this.offset = offset;
		this.limit = limit;
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
	
	public ListDto<T> manipulteTotal(int newTotal) {
		this.total = newTotal;
		return this;
	}
	
	public ArrayList<T> getResults() {
		return results;
	}
	
}
