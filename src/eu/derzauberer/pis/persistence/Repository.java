package eu.derzauberer.pis.persistence;

import java.util.Collection;
import java.util.Optional;

public interface Repository<T> {

	String getName();
	
	Class<T> getType();
	
	Optional<T> getById(String id);
	
	boolean containsById(String id);
	
	int size();
	
	default boolean isEmpty() {
		return size() == 0;
	}
	
	Collection<Lazy<T>> getAll();
	
	void save(T entity);
	
	boolean removeById(String id);
	
}
