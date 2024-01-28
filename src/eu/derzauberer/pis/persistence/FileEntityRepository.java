package eu.derzauberer.pis.persistence;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.structure.model.EntityModel;
import eu.derzauberer.pis.structure.model.NameEntityModel;

public class FileEntityRepository<T extends EntityModel<T>> implements EntityRepository<T> {
	
	private final String name;
	private final Class<T> type;
	private final JsonFileHandler<T> fileHandler;
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(FileEntityRepository.class);

	public FileEntityRepository(String name, Class<T> type) {
		this.name = name;
		this.type = type;
		this.fileHandler = new JsonFileHandler<>(name, name + "/entities", type, LOGGER);
		LOGGER.info("Loaded {} {}", size(), name);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<T> getType() {
		return type;
	}

	@Override
	public Optional<T> getById(String id) {
		if (id == null) return Optional.empty();
		return fileHandler.loadAsOptional(id);
	}
	
	@Override
	public boolean containsById(String id) {
		Objects.requireNonNull(id);
		return fileHandler.exists(id);
	}

	@Override
	public int size() {
		return fileHandler.size();
	}

	@Override
	public Stream<Lazy<T>> stream() {
		return fileHandler.stream();
	}
	
	@Override
	public void save(T entity) {
		Objects.requireNonNull(entity);
		Objects.requireNonNull(entity.getId());
		if (entity instanceof NameEntityModel) Objects.requireNonNull(((NameEntityModel) entity).getName());
		fileHandler.save(entity.getId(), entity);
	}
	
	@Override
	public boolean removeById(String id) {
		Objects.requireNonNull(id);
		return fileHandler.delete(id);
	}

}
