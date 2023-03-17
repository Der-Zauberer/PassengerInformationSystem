package eu.derzauberer.pis.util;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

import eu.derzauberer.pis.model.Entity;

public class FileRepository<T extends Entity> extends Repository<T> {

	public FileRepository(String name, Class<T> type) {
		super(name, type);
		LOGGER.info("Loaded {} {}", size(), name);
	}

	@Override
	public void add(T entity) {
		saveEntity(entity);
	}

	@Override
	public void removeById(String id) {
		deleteEnity(id);
	}

	@Override
	public boolean containsById(String id) {
		return containsEntity(id);
	}

	@Override
	public Optional<T> getById(String id) {
		return Optional.empty();
	}

	@Override
	public Collection<T> getAll() {
		return loadEntities();
	}
	
	@Override
	public int size() {
		return new File(DIRECTORY, getName()).list().length;
	}

}
