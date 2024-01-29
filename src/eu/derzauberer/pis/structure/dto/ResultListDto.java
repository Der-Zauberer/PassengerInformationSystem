package eu.derzauberer.pis.structure.dto;

import java.util.Collection;
import java.util.List;

import eu.derzauberer.pis.persistence.Lazy;

public class ResultListDto<T> {
	
	private final int offset;
    private final int limit;
    private final int total;
    private final List<T> results;
    
	public ResultListDto(int offset, int limit, Collection<Lazy<T>> collection) {
		if (offset < 0) throw new IllegalArgumentException("The offset must be positive!");
		if (limit < 1) throw new IllegalArgumentException("The limit must be larger than zero!");
		this.offset = offset;
		this.limit = limit;
		this.total = collection.size();
		this.results = collection.stream().skip(offset).limit(limit).map(Lazy::get).toList();
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
	
	public List<T> getResults() {
		return results;
	}
	
}
