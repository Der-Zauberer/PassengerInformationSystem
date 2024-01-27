package eu.derzauberer.pis.persistence;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.structure.model.EntityModel;
import eu.derzauberer.pis.structure.model.NameEntityModel;
import eu.derzauberer.pis.util.ProgressStatus;

public class MemoryEntityRepository<T extends EntityModel<T>> implements EntityRepository<T> {
	
	private final String name;
	private final Class<T> type;
	private final JsonFileHandler<T> fileHandler;
	private final Map<String, T> entities;
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(MemoryEntityRepository.class);

	public MemoryEntityRepository(String name, Class<T> type) {
		this.name = name;
		this.type = type;
		this.fileHandler = new JsonFileHandler<>(name, name + "/entities", type, LOGGER);
		this.entities = new TreeMap<>();
		
		final ProgressStatus progress = new ProgressStatus(name, fileHandler.size());
		fileHandler.stream()
				.map(LazyFile::load)
				.map(entity -> {progress.count(); return entity;})
				.forEach(entity -> this.entities.put(entity.getId(), entity));
		LOGGER.info("Loaded {} {}", entities.size(), name);
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
		final T entity = entities.get(id);
		if (entity == null) return Optional.empty();
		return Optional.of(entity.copy());
	}
	
	@Override
	public boolean containsById(String id) {
		Objects.requireNonNull(id);
		return entities.containsKey(id);
	}
	
	@Override
	public int size() {
		return entities.size();
	}
	
	@Override
	public Stream<LazyFile<T>> stream() {
		return entities.values().stream().map(entity -> new LazyFile<>(Path.of(fileHandler.getDirectory(), entity.getId()), path -> entity));
	}
	
	@Override
	public void save(T entity) {
		Objects.requireNonNull(entity);
		Objects.requireNonNull(entity.getId());
		if (entity instanceof NameEntityModel) Objects.requireNonNull(((NameEntityModel) entity).getName());
		final T copy = entity.copy();
		entities.put(entity.getId(), copy);
		fileHandler.save(copy.getId(), copy);
	}
	
	@Override
	public boolean removeById(String id) {
		Objects.requireNonNull(id);
		final boolean exist = entities.containsKey(id);
		entities.remove(id);
		fileHandler.delete(id);
		return exist;
	}

}
