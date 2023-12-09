package eu.derzauberer.pis.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.structure.model.Entity;
import eu.derzauberer.pis.structure.model.NameEntity;

public class MemoryEntityRepository<T extends Entity<T>> extends EntityRepository<T>{
	
	private final Map<String, T> entities = new TreeMap<>();
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(MemoryEntityRepository.class);

	public MemoryEntityRepository(String name, Class<T> type) {
		super(name, type, LOGGER);
		final List<T> entities = loadEntities(true);
		for (T entity : entities) {
			this.entities.put(entity.getId(), entity);
		}
		LOGGER.info("Loaded {} {}", size(), name);
	}
	
	@Override
	public void save(T entity) {
		Objects.requireNonNull(entity);
		Objects.requireNonNull(entity.getId());
		if (entity instanceof NameEntity) Objects.requireNonNull(((NameEntity) entity).getName());
		final T copy = entity.copy();
		entities.put(entity.getId(), copy);
		saveEntity(copy);
	}
	
	@Override
	public boolean removeById(String id) {
		Objects.requireNonNull(id);
		final boolean exist = entities.containsKey(id);
		entities.remove(id);
		deleteEnity(id);
		return exist;
	}
	
	@Override
	public boolean containsById(String id) {
		Objects.requireNonNull(id);
		return entities.containsKey(id);
	}
	
	@Override
	public Optional<T> getById(String id) {
		if (id == null) return Optional.empty();
		final T entity = entities.get(id);
		if (entity == null) return Optional.empty();
		return Optional.of(entity.copy());
	}
	
	@Override
	public List<T> getAll() {
		return Collections.unmodifiableList(entities.values().stream().toList());
	}
	
	@Override
	public List<T> getRange(int beginn, int end) {
		final List<T> list = this.entities.values().stream().toList();
		final List<T> entities = new ArrayList<>();
		for (int i = beginn; i < end; i++) {
			entities.add(list.get(i));
		}
		return entities;
	}

	@Override
	public int size() {
		return entities.size();
	}

}
