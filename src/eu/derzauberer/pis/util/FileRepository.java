package eu.derzauberer.pis.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.model.Entity;

public class FileRepository<T extends Entity<I>, I> {
	
	private final String DIRECTORY = "data";
	private final String FILE_TYPE = ".json";
	private final String name;
	private final Class<T> type;
	private boolean initiaized;
	private final Set<I> entities = new HashSet<>();
	
	private static final ObjectMapper MAPPER = Pis.getSpringConfig().getJsonMapperBuilder().build();
	private static final Logger LOGGER = LoggerFactory.getLogger(FileRepository.class.getSimpleName());
	
	public FileRepository(String name, Class<T> type) {
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
					registerEntity(path);
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
		entities.add(entity.getId());
		try {
			saveEntity(entity);
		} catch (IOException exception) {
			LOGGER.warn("Couldn't save entity {} from {}: {} {}", entity.getId(), name, exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
	
	public void remove(T entity) {
		remove(entity.getId());
	}
	
	public void remove(I id) {
		entities.remove(id);
		try {
			if (Files.deleteIfExists(Paths.get(DIRECTORY, name, id.toString() + FILE_TYPE))) {
			}
		} catch (IOException exception) {
			LOGGER.error("Couldn't remove {} from {}: {} {}!", id, name, exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
	
	public Optional<T> get(I id) {
		try {
			return loadEntity(id);
		} catch (IOException exception) {
			LOGGER.error("Couldn't load {} from {}: {} {}", id, name, exception.getClass().getSimpleName(), exception.getMessage());
			return Optional.empty();
		}
	}
	
	public List<T> getAll() {
		final List<T> entities = new ArrayList<>();
		for (I id : this.entities) {
			get(id).ifPresent(entities::add);
		}
		return entities;
	}
	
	public boolean contains(I id) {
		return entities.contains(id);
	}
	
	public void packageEntities(Path path) {
		try {
			final List<T> entities = getAll();
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
	
	private void registerEntity(Path path) throws IOException {
		final String content = Files.readString(path);
		final T entity = MAPPER.readValue(content, type);
		entities.add(entity.getId());
	}
	
	private Optional<T> loadEntity(I id) throws IOException {
		final Path path = Paths.get(DIRECTORY, name, id.toString() + FILE_TYPE);
		if (!entities.contains(id) || !Files.exists(path)) return Optional.empty();
		final String content = Files.readString(path);
		final T entity = MAPPER.readValue(content, type);
		return Optional.of(entity);
	}
	
	private void saveEntity(T entity) throws IOException {
		final Path path = Paths.get(DIRECTORY, name, entity.getId().toString() + FILE_TYPE);
		final String content = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(entity);
		Files.writeString(path, content);
	}

}
