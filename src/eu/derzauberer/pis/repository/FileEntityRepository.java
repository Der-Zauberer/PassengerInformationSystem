package eu.derzauberer.pis.repository;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.structure.model.EntityModel;
import eu.derzauberer.pis.structure.model.NameEntityModel;

public class FileEntityRepository<T extends EntityModel<T>> extends EntityRepository<T> {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(FileEntityRepository.class);

	public FileEntityRepository(String name, Class<T> type) {
		super(name, type, LOGGER);
		LOGGER.info("Loaded {} {}", size(), name);
	}

	@Override
	public void save(T entity) {
		Objects.requireNonNull(entity);
		Objects.requireNonNull(entity.getId());
		if (entity instanceof NameEntityModel) Objects.requireNonNull(((NameEntityModel) entity).getName());
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
		return getById(id).isPresent();
	}

	@Override
	public Optional<T> getById(String id) {
		if (id == null) return Optional.empty();
		return loadEntity(id);
	}

	@Override
	public List<T> getAll() {
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
