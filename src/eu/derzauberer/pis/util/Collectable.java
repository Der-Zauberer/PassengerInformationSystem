package eu.derzauberer.pis.util;

import java.util.List;
import java.util.function.Function;

import eu.derzauberer.pis.dto.ListDto;
import eu.derzauberer.pis.dto.PageDto;

public interface Collectable<T> {
	
	boolean isEmpty();
	
	int size();
	
	List<T> getAll();
	
	List<T> getRange(int beginn, int end);
	
	default <R> Collectable<R> map(Function<T, R> mapping) {
		return new CollectableMap<>(this, mapping);
	}
	
	default ListDto<T> getList(int offset, int limit) {
		if (offset < 0) throw new IllegalArgumentException("Offset has to be larger or same than zero!");
		if (limit < 1) throw new IllegalArgumentException("Limit has to be larger than zero!");
		if (offset != 0 && offset >= size()) {
			throw new IllegalArgumentException("The offset is larger than the total amount of results!");
		}
		int sum = offset + limit;
		int max = sum > size() ? max = size() : sum;
		final ListDto<T> listDto = new ListDto<>();
		listDto.setOffset(offset);
		listDto.setLimit(sum > size() ? size() - offset : limit);
		listDto.setTotal(size());
		listDto.setResults(getRange(offset, max));
		return listDto;
	}
	
	default PageDto<T> getPage(int page, int pageSize) {
		if (page < 1) throw new IllegalArgumentException("Page has to be larger than zero!");
		if (pageSize < 1) throw new IllegalArgumentException("PageSize has to be larger than zero!");
		final double pageRawAmount = Math.ceil(size() / pageSize);
		final int pageAmount = (int) pageRawAmount + (pageRawAmount % 1 == 0 && pageRawAmount != 0 ? 0 : 1);
		if (page > pageAmount) throw new IllegalArgumentException("The page is larger than the total amount of pages!");
		final int offset = (page - 1) * pageSize;
		final int max = (page == pageAmount) ? size() : offset + pageSize;
		final PageDto<T> pageDto = new PageDto<>();
		pageDto.setPage(page);
		pageDto.setPageSize(pageSize);
		pageDto.setPageAmount(pageAmount);
		pageDto.setResults(getRange(offset, max));
		return pageDto;
	}

}
