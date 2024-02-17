package eu.derzauberer.pis.persistence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.derzauberer.pis.configuration.SpringConfiguration;

public class JsonFileHandler<T> {
	
	private final String name;
	private final String directory;
	private final Class<T> type;
	private final Logger logger;
	
	private static final String FILE_TYPE = ".json";
	private static final ObjectMapper OBJECT_MAPPER = SpringConfiguration.getBean(ObjectMapper.class);
	
	public JsonFileHandler(String name, String directory, Class<T> type, Logger logger) {
		this.name = name;
		this.directory = "data/" + directory;
		this.type = type;
		this.logger = logger;
		try {
			Files.createDirectories(Paths.get(this.directory));
		} catch (IOException exception) {
			logger.error("Couldn't create directory {}: {} {}", this.directory, exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
	
	public String getDirectory() {
		return directory;
	}
	
	public Optional<T> load(String name) {
		return load(name, type);
	}
	
	public <R> Optional<R> load(String name, Class<R> type) {
		return Optional.ofNullable(load(getPath(name), type));
	}
	
	public Stream<Lazy<T>> stream() {
		return stream(type);
	}
	
	public <R> Stream<Lazy<R>> stream(Class<R> type) {
		try {
			return Files.list(Paths.get(directory)).map(path -> new Lazy<>(getFileNameFromPath(path), () -> load(path, type)));
		} catch (IOException exception) {
			logger.error("Couldn't load {}: {} {}", this.name, exception.getClass().getSimpleName(), exception.getMessage());
			return new ArrayList<Lazy<R>>().stream();
		}
	}
	
	private <R> R load(Path path, Class<R> type) {
		try {
			if (!Files.exists(path)) return null;
			final String content = Files.readString(path);
			final R object = OBJECT_MAPPER.readValue(content, type);
			return object;
		} catch (IOException exception) {
			logger.error("Couldn't load {} from {}: {} {}", name, this.name, exception.getClass().getSimpleName(), exception.getMessage());
			return null;
		}
	}
	
	public void save(String name, Object object) {
		try {
			Files.createDirectories(Paths.get(directory));
			final String content = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
			Files.writeString(getPath(name), content);
		} catch (IOException exception) {
			logger.error("Couldn't save {} in {}: {} {}", name, this.name, exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
	
	public boolean delete(String name) {
		try {
			return Files.deleteIfExists(getPath(name));
		} catch (IOException exception) {
			logger.error("Couldn't delete {} from {}: {} {}", name, this.name, exception.getClass().getSimpleName(), exception.getMessage());
		}
		return false;
	}
	
	public boolean exists(String name) {
		try {
			return Files.deleteIfExists(getPath(name));
		} catch (IOException exception) {
			logger.error("Couldn't check for {} from {}: {} {}", name, this.name, exception.getClass().getSimpleName(), exception.getMessage());
		}
		return false;
	}
	
	public int size() {
		return new File(directory).list().length;
	}
	
	private Path getPath(String name) {
		return Paths.get(directory, name + FILE_TYPE);
	}
	
	private String getFileNameFromPath(Path path) {
		return path.getFileName().toString().replaceFirst("[.][^.]+$", "");
	}

}
