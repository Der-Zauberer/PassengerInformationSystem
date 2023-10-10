package eu.derzauberer.pis.components;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.derzauberer.pis.configuration.SpringConfiguration;
import eu.derzauberer.pis.service.EntityService;

public abstract class Component<T extends EntityService<?>, M> {

	private final T service;
	private final String name;
	private final Logger logger;
	
	private static final String DIRECTORY = "data";
	private static final String FILE_TYPE = ".json";
	private static final ObjectMapper OBJECT_MAPPER = SpringConfiguration.getBean(ObjectMapper.class);
	
	public Component(String name, T service, Logger logger) {
		this.service = service;
		this.name = name;
		this.logger = logger;
	}
	
	public T getService() {
		return service;
	}
	
	public void save(M model) {
		try {
			final Path path = Paths.get(DIRECTORY, name, service.getName() + FILE_TYPE);
			Files.createDirectories(Paths.get(DIRECTORY, name));
			final String content = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(model);
			Files.writeString(path, content);
		} catch (IOException exception) {
			logger.warn("Couldn't save {} with id {}: {} {}", getClass().getSimpleName(), service.getName(), exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
	
	public M load(Class<M> type) {
		try {
			final Path path = Paths.get(DIRECTORY, name, service.getName() + FILE_TYPE);
			if (!Files.exists(path)) return null;
			final String content = Files.readString(path);
			final M model = OBJECT_MAPPER.readValue(content, type);
			return model;
		} catch (IOException exception) {
			logger.error("Couldn't load {} with id {}: {} {}!", getClass().getSimpleName(), service.getName(), exception.getClass().getSimpleName(), exception.getMessage());
			return null;
		}
	}
	
	public Optional<M> loadAsOptional(Class<M> type) {
		try {
			final Path path = Paths.get(DIRECTORY, name, service.getName() + FILE_TYPE);
			if (!Files.exists(path)) return Optional.empty();
			final String content = Files.readString(path);
			final M model = OBJECT_MAPPER.readValue(content, type);
			return Optional.of(model);
		} catch (IOException exception) {
			logger.error("Couldn't load {} with id {}: {} {}!", getClass().getSimpleName(), service.getName(), exception.getClass().getSimpleName(), exception.getMessage());
			return Optional.empty();
		}
	}
	
}
