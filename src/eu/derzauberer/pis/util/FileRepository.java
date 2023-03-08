package eu.derzauberer.pis.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.serialization.DateDeserializer;
import eu.derzauberer.pis.serialization.DateSerializer;
import eu.derzauberer.pis.serialization.DateTimeDeserializer;
import eu.derzauberer.pis.serialization.DateTimeSerializer;
import eu.derzauberer.pis.serialization.TimeDeserializer;
import eu.derzauberer.pis.serialization.TimeSerializer;

public class FileRepository<T extends Entity<I>, I> {
	
	private final String DIRECTORY = "data";
	private final String FILE_TYPE = ".json";
	private final String name;
	private final Class<T> type;
	private final Set<I> entities = new HashSet<>();
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final Logger LOGGER = LoggerFactory.getLogger(FileRepository.class);
	
	static {
		LOGGER.info("Loading data...");
		SimpleModule module = new SimpleModule();
		module.addSerializer(LocalTime.class, new TimeSerializer());
		module.addDeserializer(LocalTime.class, new TimeDeserializer());
		module.addSerializer(LocalDate.class, new DateSerializer());
		module.addDeserializer(LocalDate.class, new DateDeserializer());
		module.addSerializer(LocalDateTime.class, new DateTimeSerializer());
		module.addDeserializer(LocalDateTime.class, new DateTimeDeserializer());
		MAPPER.registerModule(module);
		MAPPER.setSerializationInclusion(Include.NON_EMPTY);
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		MAPPER.setVisibility(MAPPER.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.ANY));
	}
	
	public FileRepository(String name, Class<T> type) {
		this.name = name;
		this.type = type;
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
			LOGGER.error("Couldn't load {}: ", name, exception.getMessage());
		}
	}
	
	public void add(T entity) {
		entities.add(entity.getId());
		try {
			saveEntity(entity);
		} catch (IOException exception) {
			LOGGER.warn("Couldn't save entity {} from {}: {}", name, entity.getId(), exception.getMessage());
		}
	}
	
	public void remove(T entity) {
		remove(entity.getId());
	}
	
	public void remove(I id) {
		entities.remove(id);
		try {
			Files.deleteIfExists(Paths.get(DIRECTORY, name, id.toString() + FILE_TYPE));
		} catch (IOException exception) {
			LOGGER.error("Couldn't remove entity {} from {}: !", name, id, exception.getMessage());
		}
	}
	
	public Optional<T> get(I id) {
		try {
			return loadEntity(id);
		} catch (IOException exception) {
			LOGGER.error("Couldn't load entity {} from {}: ", name, id, exception.getMessage());
			return Optional.empty();
		}
	}
	
	public boolean contains(I id) {
		return entities.contains(id);
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
