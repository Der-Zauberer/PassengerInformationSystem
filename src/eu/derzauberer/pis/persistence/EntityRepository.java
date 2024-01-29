package eu.derzauberer.pis.persistence;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.structure.model.EntityModel;
import eu.derzauberer.pis.structure.model.NameEntityModel;
import eu.derzauberer.pis.util.ProgressStatus;

public class EntityRepository<T extends EntityModel<T>> implements Repository<T> {
	
	private final String name;
	private final Class<T> type;
	private final boolean eagerLoading;
	private final JsonFileHandler<T> fileHandler;
	private final Map<String, Lazy<T>> entities;
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(Repository.class);
	
	public EntityRepository(String name, Class<T> type, boolean eagerLoading) {
		this.name = name;
		this.type = type;
		this.eagerLoading = eagerLoading;
		this.fileHandler = new JsonFileHandler<>(name, name + "/entities", type, LOGGER);
		this.entities = new TreeMap<>();
		
		final ProgressStatus progress = new ProgressStatus(name, fileHandler.size());
		if (eagerLoading) {
			fileHandler.stream()
			.map(Lazy::get)
			.map(entity -> {progress.count(); return entity;})
			.forEach(entity -> this.entities.put(entity.getId(), new Lazy<>(entity.getId(), () -> entity)));
		} else {
			fileHandler.stream().forEach(lazy -> this.entities.put(lazy.getId(), lazy));
		}
		
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
		final Lazy<T> entity = entities.get(id);
		if (entity == null) return Optional.empty();
		return Optional.of(entity.get().copy());
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
	public Collection<Lazy<T>> getAll() {
		return entities.values();
	}
	
	@Override
	public void save(T entity) {
		Objects.requireNonNull(entity);
		Objects.requireNonNull(entity.getId());
		if (entity instanceof NameEntityModel) Objects.requireNonNull(((NameEntityModel) entity).getName());
		
		fileHandler.save(entity.getId(), entity);
		
		if (eagerLoading) {
			final T copy = entity.copy();
			entities.put(entity.getId(), new Lazy<>(entity.getId(), () -> copy));
		} else {
			entities.put(entity.getId(), new Lazy<>(entity.getId(), () -> fileHandler.load(entity.getId())));
		}
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
