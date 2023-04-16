package eu.derzauberer.pis.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;

import eu.derzauberer.pis.main.Pis;

public class MemoryRepository<T extends Entity<T>> extends Repository<T>{
	
	private final Map<String, T> entities = new HashMap<>();
	private final Map<String, Long> lastTimeUpdated = new HashMap<>();
	private static final ModelMapper MODEL_MAPPER = Pis.getSpringConfig().getModelMapper();
	
	public MemoryRepository(String name, Class<T> type) {
		super(name, type);
		final List<T> entities = loadEntities(true);
		for (T entity : entities) {
			this.entities.put(entity.getId(), entity);
			lastTimeUpdated.put(entity.getId(), getLastUpdated(entity.getId()));
		}
		LOGGER.info("Loaded {} {}", size(), name);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void add(T entity) {
		if (lastTimeUpdated.get(entity.getId()) != getLastUpdated(entity.getId())) {
			throw new ConcurrentModificationException("The entity " + getName()+ " with id " + entity.getId() + " has already changed!");
		}
		final T copy = (T) MODEL_MAPPER.map(entity, entity.getClass());
		entities.put(entity.getId(), copy);
		lastTimeUpdated.put(entity.getId(), getLastUpdated(entity.getId()));
		saveEntity(copy);
	}
	
	@Override
	public boolean removeById(String id) {
		final boolean exist = entities.containsKey(id);
		entities.remove(id);
		deleteEnity(id);
		return exist;
	}
	
	@Override
	public boolean containsById(String id) {
		return entities.containsKey(id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Optional<T> getById(String id) {
		if (lastTimeUpdated.get(id) != getLastUpdated(id)) {
			loadEntity(id).ifPresent(entity -> {
				entities.put(id, entity); 
				lastTimeUpdated.put(entity.getId(), getLastUpdated(entity.getId()));
			});
		}
		final T entity = entities.get(id);
		if (entity == null) return Optional.empty();
		return Optional.of((T) MODEL_MAPPER.map(entities.get(id), entity.getClass()));
	}
	
	@Override
	public List<T> getList() {
		for (String id : entities.keySet()) {
			if (lastTimeUpdated.get(id) != getLastUpdated(id)) {
				loadEntity(id).ifPresent(entity -> { 
					entities.put(id, entity); 
					lastTimeUpdated.put(entity.getId(), getLastUpdated(entity.getId()));
				});
			}
		}
		return Collections.unmodifiableList(entities.values().stream().sorted().toList());
	}

	@Override
	public int size() {
		return entities.size();
	}
	
	public long getLastUpdated(String id) {
		final Path path = Paths.get(DIRECTORY, getName(), id + FILE_TYPE);
		if (!Files.exists(path)) return -1;
		try {
			return Files.readAttributes(path, BasicFileAttributes.class).lastModifiedTime().toMillis();
		} catch (IOException exception) {
			return -1;
		}
	}

}
