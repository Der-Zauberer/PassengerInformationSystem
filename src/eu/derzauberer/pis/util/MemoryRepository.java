package eu.derzauberer.pis.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.main.Pis;

public class MemoryRepository<T extends Entity<T>> extends Repository<T>{
	
	private final Map<String, T> entities = new HashMap<>();
	private static final ModelMapper MODEL_MAPPER = Pis.getSpringConfig().getModelMapper();
	protected static final Logger LOGGER = LoggerFactory.getLogger(MemoryRepository.class);
	
	public MemoryRepository(String name, Class<T> type) {
		super(name, type, LOGGER);
		final List<T> entities = loadEntities(true);
		for (T entity : entities) {
			this.entities.put(entity.getId(), entity);
		}
		LOGGER.info("Loaded {} {}", size(), name);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void add(T entity) {
		final T copy = (T) MODEL_MAPPER.map(entity, entity.getClass());
		entities.put(entity.getId(), copy);
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
		if (hasEntityUpdatedById(id)) {
			loadEntity(id).ifPresentOrElse(entity -> entities.put(id, entity), () -> entities.remove(id));
		}
		final T entity = entities.get(id);
		if (entity == null) return Optional.empty();
		return Optional.of((T) MODEL_MAPPER.map(entities.get(id), entity.getClass()));
	}
	
	@Override
	public List<T> getList() {
		try {
			for (Path path : Files.list(Paths.get(DIRECTORY, getName())).toList()) {
				final String fileName = path.getFileName().toString();
				final String id = fileName.substring(0, fileName.length() - FILE_TYPE.length());
				if (hasEntityUpdatedById(id)) {
					loadEntity(id).ifPresentOrElse(entity -> entities.put(id, entity), () -> entities.remove(id));
				}
			}
		} catch (IOException exception) {
			LOGGER.error("Couldn't load entities from {}: {} {}!", getName(), exception.getClass().getSimpleName(), exception.getMessage());
		}
		final List<String> idsToRemove = new ArrayList<>();
		for (String id : entities.keySet()) {
			if (hasEntityUpdatedById(id)) {
				loadEntity(id).ifPresentOrElse(entity -> entities.put(id, entity), () -> idsToRemove.add(id));
			}
		}
		for (String id : idsToRemove) {
			entities.remove(id);
		}
		return Collections.unmodifiableList(entities.values().stream().sorted().toList());
	}

	@Override
	public int size() {
		return entities.size();
	}

}
