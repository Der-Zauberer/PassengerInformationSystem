package eu.derzauberer.pis.util;

public interface SearchComparator<T> {
	
	int compare(String search, T o1, T o2);

}
