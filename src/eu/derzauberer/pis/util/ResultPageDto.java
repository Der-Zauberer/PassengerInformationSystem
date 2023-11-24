package eu.derzauberer.pis.util;

import java.util.List;

public class ResultPageDto<T> {
	
	private int page;
    private int pageSize;
    private int pageAmount;
    private List<T> results;
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getPageAmount() {
		return pageAmount;
	}
	
	public void setPageAmount(int pageAmount) {
		this.pageAmount = pageAmount;
	}
	
	public List<T> getResults() {
		return results;
	}
	
	public void setResults(List<T> results) {
		this.results = results;
	}
	
	@Override
	public String toString() {
		return results.toString();
	}

}
