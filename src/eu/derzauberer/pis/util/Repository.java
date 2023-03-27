package eu.derzauberer.pis.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.derzauberer.pis.main.Pis;

public abstract class Repository<T extends Entity<?>> {
	
	private final String name;
	private final Class<T> type;
	
	protected static final String DIRECTORY = "data";
	protected static final String FILE_TYPE = ".json";
	protected static final ObjectMapper OBJECT_MAPPER = Pis.getSpringConfig().getObjectMapper();
	protected static final Logger LOGGER = LoggerFactory.getLogger(Repository.class.getSimpleName());
	
	public Repository(String name, Class<T> type) {
		this.name = name;
		this.type = type;
		try {
			Files.createDirectories(Paths.get(DIRECTORY, name));
		} catch (IOException exception) {
			LOGGER.error("Couldn't create directory {}: {} {}", DIRECTORY + "/" + name, exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
	
	public String getName() {
		return name;
	}
	
	public Class<T> getType() {
		return type;
	}
	
	public abstract void add(T entity);
	
	public abstract boolean removeById(String id);
	
	public abstract boolean containsById(String id);
	
	public abstract Optional<T> getById(String id);
	
	public abstract List<T> getList();
	
	public abstract int size();
	
	public void packageEntities(Path path) {
		try {
			final Collection<T> entities = getList();
			final String content = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(entities);
			Files.writeString(path, content);
			LOGGER.info("Extracted {} {}", entities.size(), name);
		} catch (IOException exception) {
			LOGGER.error("Couldn't package {}: {} {}",  name, exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
	
	public void extractEntities(Path path) {
		try {
			final String content = Files.readString(path);
			final List<T> entities = OBJECT_MAPPER.readValue(content, new TypeReference<ArrayList<T>>() {});
			entities.forEach(this::add);
			LOGGER.info("Extracted {} {}", entities.size(), name);
		} catch (IOException exception) {
			LOGGER.error("Couldn't extract {}: {} {}",  name, exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
	
	protected List<T> loadEntities() {
		return loadEntities(false);
	}
	
	protected List<T> loadEntities(boolean progress) {
		try {
			final List<T> entities = new ArrayList<>();
			final ProgressStatus progressStatus = new ProgressStatus(name, new File(DIRECTORY, getName()).list().length);
			for (Path path : Files.list(Paths.get(DIRECTORY, name)).toList()) {
				if (!Files.exists(path)) continue;
				final String content = Files.readString(path);
				final T entity = OBJECT_MAPPER.readValue(content, type);
				entities.add(entity);
				if (progress) progressStatus.count();
			}
			return entities;
		} catch (IOException exception) {
			LOGGER.error("Couldn't load entities {}: {} {}!", getName(), exception.getClass().getSimpleName(), exception.getMessage());
			return new ArrayList<>();
		}
	}
	
	protected boolean containsEntity(String id) {
		final Path path = Paths.get(DIRECTORY, name, id.toString() + FILE_TYPE);
		return Files.exists(path);
	}
	
	protected Optional<T> loadEntity(String id) {
		try {
			final Path path = Paths.get(DIRECTORY, name, id.toString() + FILE_TYPE);
			if (Files.exists(path)) return Optional.empty();
			final String content = Files.readString(path);
			final T entity = OBJECT_MAPPER.readValue(content, type);
			return Optional.of(entity);
		} catch (IOException exception) {
			LOGGER.error("Couldn't load entity {} from {}: {} {}!", id, getName(), exception.getClass().getSimpleName(), exception.getMessage());
			return Optional.empty();
		}
	}
	
	protected void saveEntity(T entity) {
		try {
			final Path path = Paths.get(DIRECTORY, name, entity.getId().toString() + FILE_TYPE);
			final String content = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(entity);
			Files.writeString(path, content);
		} catch (IOException exception) {
			LOGGER.warn("Couldn't save entity {} from {}: {} {}", entity.getId(), getName(), exception.getClass().getSimpleName(), exception.getMessage());
		}
		
	}
	
	protected void deleteEnity(String id) {
		try {
			Files.deleteIfExists(Paths.get(DIRECTORY, name, id + FILE_TYPE));
		} catch (IOException exception) {
			LOGGER.warn("Couldn't delete entity {} from {}: {} {}", id, getName(), exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
	
}
