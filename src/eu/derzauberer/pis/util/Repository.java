package eu.derzauberer.pis.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.model.Entity;

public class Repository<T extends Entity> {
	
	private final String DIRECTORY = "data";
	private final String FILE_TYPE = ".json";
	private final String name;
	private final Class<T> type;
	private boolean initiaized;
	private final Map<String, T> entities = new HashMap<>();
	
	private static final ObjectMapper MAPPER = Pis.getSpringConfig().getJsonMapperBuilder().build();
	private static final Logger LOGGER = LoggerFactory.getLogger(Repository.class.getSimpleName());
	
	public Repository(String name, Class<T> type) {
		this.name = name;
		this.type = type;
		this.initiaized = false;
	}
	
	public String getName() {
		return name;
	}
	
	public Class<T> getType() {
		return type;
	}
	
	public boolean isInitiaized() {
		return initiaized;
	}
	
	public void initialize() {
		if (initiaized) return;
		initiaized = true;
		try {
			Files.createDirectories(Paths.get(DIRECTORY, name));
			int counter = 0;
			for (Path path : Files.list(Paths.get(DIRECTORY, name)).toList()) {
				try {
					loadEntity(path);
					counter++;
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
			LOGGER.info("Loaded {} {}", counter, name);
		} catch (IOException exception) {
			LOGGER.error("Couldn't load {}: {} {}", name, exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
	
	public void add(T entity) {
		entities.put(entity.getId(), entity);
		try {
			saveEntity(entity);
		} catch (IOException exception) {
			LOGGER.warn("Couldn't save entity {} from {}: {} {}", entity.getId(), name, exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
	
	public void remove(T entity) {
		remove(entity.getId());
	}
	
	public void remove(String id) {
		entities.remove(id);
		try {
			if (Files.deleteIfExists(Paths.get(DIRECTORY, name, id.toString() + FILE_TYPE))) {
			}
		} catch (IOException exception) {
			LOGGER.error("Couldn't remove {} from {}: {} {}!", id, name, exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
	
	public Optional<T> get(String id) {
		return Optional.ofNullable(entities.get(id));
	}
	
	public Collection<T> getAll() {
		return entities.values();
	}
	
	public boolean contains(String id) {
		return entities.containsKey(id);
	}
	
	public void packageEntities(Path path) {
		try {
			final Collection<T> entities = getAll();
			final String content = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(entities);
			Files.writeString(path, content);
			LOGGER.info("Extracted {} {}", entities.size(), name);
		} catch (IOException exception) {
			LOGGER.error("Couldn't package {}: {} {}",  name, exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
	
	public void extractEntities(Path path) {
		try {
			final String content = Files.readString(path);
			final List<T> entities = MAPPER.readValue(content, new TypeReference<ArrayList<T>>() {});
			entities.forEach(this::add);
			LOGGER.info("Extracted {} {}", entities.size(), name);
		} catch (IOException exception) {
			LOGGER.error("Couldn't extract {}: {} {}",  name, exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
	
	private void loadEntity(Path path) throws IOException {
		final String content = Files.readString(path);
		final T entity = MAPPER.readValue(content, type);
		entities.put(entity.getId(), entity);
	}
	
	private void saveEntity(T entity) throws IOException {
		final Path path = Paths.get(DIRECTORY, name, entity.getId().toString() + FILE_TYPE);
		final String content = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(entity);
		Files.writeString(path, content);
	}

}
