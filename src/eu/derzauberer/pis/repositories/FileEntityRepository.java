package eu.derzauberer.pis.repositories;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.model.Entity;

public class FileEntityRepository<T extends Entity<T>> extends EntityRepository<T> {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(FileEntityRepository.class);

	public FileEntityRepository(String name, Class<T> type) {
		super(name, type, LOGGER);
		LOGGER.info("Loaded {} {}", size(), name);
	}

	@Override
	public void add(T entity) {
		Objects.requireNonNull(entity);
		Objects.requireNonNull(entity.getId());
		saveEntity(entity);
	}

	@Override
	public boolean removeById(String id) {
		Objects.requireNonNull(id);
		final boolean exists = containsById(id);
		deleteEnity(id);
		return exists;
	}

	@Override
	public boolean containsById(String id) {
		Objects.requireNonNull(id);
		return containsEntity(id);
	}

	@Override
	public Optional<T> getById(String id) {
		Objects.requireNonNull(id);
		return loadEntity(id);
	}

	@Override
	public List<T> getList() {
		return Collections.unmodifiableList(loadEntities());
	}
	
	@Override
	public List<T> getRange(int beginn, int end) {
		return Collections.unmodifiableList(loadEntitiesInRange(beginn, end));
	}
	
	@Override
	public int size() {
		return new File(DIRECTORY, getName()).list().length;
	}

}
