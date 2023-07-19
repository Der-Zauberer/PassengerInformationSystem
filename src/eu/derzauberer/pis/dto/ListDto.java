package eu.derzauberer.pis.dto;

import java.util.List;
import java.util.function.Function;

import eu.derzauberer.pis.service.EntityService;

public class ListDto<T> {
	
	private final int offset;
    private final int limit;
    private int total;
    private final List<T> results;
    
    public ListDto(List<T> list) {
    	this(list, 0, list.size());
    }
    
    public ListDto(List<T> list, int limit) {
    	this(list, 0, limit);
    }
	
	public ListDto(List<T> list, int offset, int limit) {
		if (offset != 0 && offset >= list.size()) {
			throw new IllegalArgumentException("The offset is larger than the total amount of results!");
		}
		this.offset = offset;
		this.limit = limit;
		this.total = list.size();
		int max = this.offset + this.limit;
		if (max >= list.size()) max = list.size();
		results = list.subList(offset, max);
	}
	
    public ListDto(EntityService<?> service) {
    	this(service, 0, service.size());
    }
    
    public ListDto(EntityService<?> service, int limit) {
    	this(service, 0, limit);
    }
	
    @SuppressWarnings("unchecked")
	public ListDto(EntityService<?> service, int offset, int limit) {
		if (offset != 0 && offset >= service.size()) {
			throw new IllegalArgumentException("The offset is larger than the total amount of results!");
		}
		this.offset = offset;
		this.limit = limit;
		this.total = service.size();
		int max = this.offset + this.limit;
		if (max >= service.size()) max = service.size();
		results = (List<T>) service.getRange(offset, max);
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
	
	public List<T> getResults() {
		return results;
	}
	
	public <R> ListDto<R> map(Function<T, R> mapper) {
		final List<R> result = getResults().stream().map(mapper).toList();
		final ListDto<R> dto = new ListDto<>(result, getOffset(), getLimit());
		dto.total = this.total;
		return dto;
	}
	
}
