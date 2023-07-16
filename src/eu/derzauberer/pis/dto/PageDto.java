package eu.derzauberer.pis.dto;

import java.util.List;
import java.util.function.Function;

import eu.derzauberer.pis.service.EntityService;

public class PageDto<T> {
	
	private final int page;
    private final int pageSize;
    private int pageAmount;
    private final List<T> results;
    
    public PageDto(List<T> list, int page) {
    	this(list, page, 100);
    }
	
	public PageDto(List<T> list, int page, int pageSize) {
		this.page = page;
		this.pageSize = pageSize;
		pageAmount = (int) Math.ceil(list.size() / pageSize) + 1;
		if (page > pageAmount) {
			throw new IllegalArgumentException("The page is larger than the total amount of pages!");
		}
		final int offset = (page - 1) * pageSize;
		final int max = (page == pageAmount) ? list.size() : offset + pageSize;
		results = list.subList(offset, max);
	}
    
    public PageDto(EntityService<?> service, int page) {
    	this(service, page, 100);
    }
	
    @SuppressWarnings("unchecked")
	public PageDto(EntityService<?> service, int page, int pageSize) {
    	this.page = page;
		this.pageSize = pageSize;
		pageAmount = (int) Math.ceil(service.size() / pageSize) + 1;
		if (page > pageAmount) {
			throw new IllegalArgumentException("The page is larger than the total amount of pages!");
		}
		final int offset = (page - 1) * pageSize;
		final int max = (page == pageAmount) ? service.size() : offset + pageSize;
		results = (List<T>) service.getRange(offset, max);
	}
	
	public int getPage() {
		return page;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public int getPageAmount() {
		return pageAmount;
	}
	
	public List<T> getResults() {
		return results;
	}
	
	public <R> PageDto<R> map(Function<T, R> mapper) {
		final List<R> result = getResults().stream().map(mapper).toList();
		final PageDto<R> dto = new PageDto<>(result, getPage(), getPageSize());
		dto.pageAmount = this.pageAmount;
		return dto;
	}

}
