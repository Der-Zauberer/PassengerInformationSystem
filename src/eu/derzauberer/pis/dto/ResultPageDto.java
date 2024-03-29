package eu.derzauberer.pis.dto;

import java.util.Collection;
import java.util.List;

import eu.derzauberer.pis.persistence.Lazy;
import lombok.Getter;

@Getter
public class ResultPageDto<T> {
	
	private final int page;
    private final int pageSize;
    private final int pageAmount;
    private final List<T> results;
    
	public ResultPageDto(int page, int pageSize, Collection<Lazy<T>> collection) {
		if (page < 1) throw new IllegalArgumentException("The page number must be larger than zero!");
		if (pageSize < 1) throw new IllegalArgumentException("The page size must be larger than zero!");
		this.page = page;
		this.pageSize = pageSize;
		final double pageRawAmount = Math.ceil(collection.size() / (double) pageSize);
		this.pageAmount = (int) pageRawAmount + (pageRawAmount % 1 == 0 && pageRawAmount != 0 ? 0 : 1);
		this.results = collection.stream().skip((page - 1) * pageSize).limit(pageSize).map(Lazy::get).toList();
	}

}
