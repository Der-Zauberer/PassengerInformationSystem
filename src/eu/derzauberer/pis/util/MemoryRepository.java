package eu.derzauberer.pis.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import eu.derzauberer.pis.model.Entity;

public class MemoryRepository<T extends Entity<?>> extends Repository<T>{
	
	private final Map<String, T> entities = new HashMap<>();
	
	public MemoryRepository(String name, Class<T> type) {
		super(name, type);
		final List<T> entities = loadEntities(true);
		for (T entity : entities) {
			this.entities.put(entity.getId(), entity);
		}
		LOGGER.info("Loaded {} {}", size(), name);
	}
	
	@Override
	public void add(T entity) {
		entities.put(entity.getId(), entity);
		saveEntity(entity);
	}
	
	@Override
	public void removeById(String id) {
		entities.remove(id);
		deleteEnity(id);
	}
	
	@Override
	public boolean containsById(String id) {
		return entities.containsKey(id);
	}
	
	@Override
	public Optional<T> getById(String id) {
		return Optional.ofNullable(entities.get(id));
	}
	
	@Override
	public List<T> getList() {
		return Collections.unmodifiableList(entities.values().stream().sorted().toList());
	}

	@Override
	public int size() {
		return entities.size();
	}

}
