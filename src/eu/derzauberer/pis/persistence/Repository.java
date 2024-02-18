package eu.derzauberer.pis.persistence;

import java.util.Optional;
import java.util.SortedSet;

public interface Repository<T> {

	String getName();
	
	Class<T> getType();
	
	Optional<T> getById(String id);
	
	boolean containsById(String id);
	
	int size();
	
	default boolean isEmpty() {
		return size() == 0;
	}
	
	SortedSet<Lazy<T>> getAll();
	
	void save(T entity);
	
	boolean removeById(String id);
	
}
