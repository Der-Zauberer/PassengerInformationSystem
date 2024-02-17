package eu.derzauberer.pis.persistence;

import java.util.Optional;

public interface EntityRepository<T> extends Repository<T> {
	
	Optional<T> getByIdOrSecondaryId(String id);

}
