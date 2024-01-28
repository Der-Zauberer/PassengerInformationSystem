package eu.derzauberer.pis.persistence;

import java.util.Optional;
import java.util.stream.Stream;

public interface EntityRepository<T> {

	String getName();
	
	Class<T> getType();
	
	Optional<T> getById(String id);
	
	boolean containsById(String id);
	
	int size();
	
	default boolean isEmpty() {
		return size() == 0;
	}
	
	Stream<Lazy<T>> stream();
	
	void save(T entity);
	
	boolean removeById(String id);
	
}
