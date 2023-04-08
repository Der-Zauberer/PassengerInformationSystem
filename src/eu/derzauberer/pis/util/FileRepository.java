package eu.derzauberer.pis.util;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FileRepository<T extends Entity<T>> extends Repository<T> {

	public FileRepository(String name, Class<T> type) {
		super(name, type);
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
