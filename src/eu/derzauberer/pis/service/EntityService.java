package eu.derzauberer.pis.service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import eu.derzauberer.pis.configuration.SpringConfiguration;
import eu.derzauberer.pis.persistence.Entity;
import eu.derzauberer.pis.persistence.EntityRepository;
import eu.derzauberer.pis.persistence.Lazy;
import eu.derzauberer.pis.persistence.Namable;

public abstract class EntityService<T extends Entity<T> & Namable> {
	
	private final EntityRepository<T> repository;
	
	private static final ObjectMapper OBJECT_MAPPER = SpringConfiguration.getBean(ObjectMapper.class);
	private static final Logger LOGGER = LoggerFactory.getLogger(EntityService.class);

	public EntityService(EntityRepository<T> repository) {
		this.repository = repository;
	}
	
	public String getName() {
		return repository.getName();
	}
	
	public T save(T entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity must not be null!");
		}
		if (entity.getId() == null || entity.getId().isEmpty()) {
			throw new IllegalArgumentException("Entity id must not be null or empty!");
		}
		if (entity.getName() == null || entity.getName().isEmpty()) {
			throw new IllegalArgumentException("Entity name must not be null or empty!");
		}
		repository.save(entity);
		return repository.getById(entity.getId()).orElseThrow(() -> new NullPointerException("Entity was deleted during save!"));
	}
	
	public boolean removeById(String id) {
		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException("Id must not be null or empty!");
		}
		return repository.removeById(id);
	}
	
	public boolean containsById(String id) {
		return repository.containsById(id);
	}
	
	public Optional<T> getById(String id) {
		return repository.getById(id);
	}
	
	public List<Lazy<T>> search(String search) {
		return repository.search(search);
	}
	
	public Collection<Lazy<T>> getAll() {
		return repository.getAll();
	}
	
	public String exportEntities() {
		try {
			final HashMap<String, Collection<T>> types = new HashMap<>();
			final List<T> entities = getAll().stream().map(Lazy::get).toList();
			types.put(getName(), entities);
			String json = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(types);
			LOGGER.info("Exported {} {}", entities.size(), getName());
			return json;
		} catch (JsonProcessingException exception) {
			LOGGER.error("Couldn't export {}: {} {}", getName(), exception.getClass().getSimpleName(), exception.getMessage());
			return null;
		}
	}
	
	public void importEntities(String json) {
		try {
			final ObjectNode jsonTypes = new ObjectMapper().readValue(json, ObjectNode.class);
			final ArrayNode jsonEntities = (ArrayNode) jsonTypes.get(getName());
			int i = 0;
			if (jsonEntities != null) {
				for (JsonNode jsonEntity : jsonEntities) {
					final T entity = OBJECT_MAPPER.readValue(jsonEntity.toString(), repository.getType());
					save(entity);
					i++;
				}
			}
			LOGGER.info("Imported {} {}", i, getName());
		} catch (IOException exception) {
			LOGGER.error("Couldn't import {}: {} {}", getName(), exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
	
}
