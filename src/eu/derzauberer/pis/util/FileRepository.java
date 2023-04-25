package eu.derzauberer.pis.util;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileRepository<T extends Entity<T>> extends Repository<T> {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(FileRepository.class);

	public FileRepository(String name, Class<T> type) {
		super(name, type, LOGGER);
		LOGGER.info("Loaded {} {}", size(), name);
	}

	@Override
	public void add(T entity) {
		saveEntity(entity);
	}

	@Override
	public boolean removeById(String id) {
		final boolean exists = containsById(id);
		deleteEnity(id);
		return exists;
	}

	@Override
	public boolean containsById(String id) {
		return containsEntity(id);
	}

	@Override
	public Optional<T> getById(String id) {
		return loadEntity(id);
	}

	@Override
	public List<T> getList() {
		return Collections.unmodifiableList(loadEntities().stream().sorted().toList());
	}
	
	@Override
	public int size() {
		return new File(DIRECTORY, getName()).list().length;
	}

}
