package de.derzauberer.pis.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.derzauberer.pis.model.Entity;

public class FileRepository<T extends Entity<I>, I> {
	
	private final String DIRECTORY = "data";
	private final String FILE_TYPE = ".json";
	private final String name;
	private final Class<T> type;
	private final Set<I> entities = new HashSet<>();
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final Logger LOGGER = LoggerFactory.getLogger(FileRepository.class);
	
	public FileRepository(String name, Class<T> type) {
		this.name = name;
		this.type = type;
		try {
			Files.createDirectories(Paths.get(DIRECTORY, name));
			int counter = 0;
			for (Path path : Files.list(Paths.get(DIRECTORY, name)).toList()) {
				try {
					final String content = Files.readString(path);
					final T entity = MAPPER.readValue(content, type);
					entities.add(entity.getId());
					counter++;
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
			LOGGER.info("Loaded {} {}.", counter, name);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public void add(T entity) {
		entities.add(entity.getId());
		try {
			final Path path = Paths.get(DIRECTORY, name, entity.getId().toString() + FILE_TYPE);
			final String content = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(entity);
			Files.writeString(path, content, StandardOpenOption.CREATE);
		} catch (IOException exception) {
			exception.printStackTrace();
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
			exception.printStackTrace();
		}
	}
	
	public Optional<T> get(I id) {
		final Path path = Paths.get(DIRECTORY, name, id.toString() + FILE_TYPE);
		if (entities.contains(id) && Files.exists(path)) {
			try {
				final String content = Files.readString(path);
				final T entity = MAPPER.readValue(content, type);
				return Optional.of(entity);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		return Optional.empty();
	}
	
	public boolean contains(I id) {
		return entities.contains(id);
	}

}
