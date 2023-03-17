package eu.derzauberer.pis.util;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.model.Entity;

public abstract class Service<T extends Entity> {
	
	private final String name;
	private Repository<T> repository;
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(Service.class.getSimpleName());

	public Service(String name, Repository<T> repository) {
		this.name = name;
		this.repository = repository;
	}
	
	public void add(T entity) {
		repository.add(entity);
	}
	
	public void removeById(String id) {
		repository.removeById(id);
	}
	
	public boolean containsById(String id) {
		return repository.containsById(id);
	}
	
	public Optional<T> getById(String id) {
		return repository.getById(id);
	}
	
	public Collection<T> getAll() {
		return repository.getAll();
	}
	
	public void packageEntities(Path path) {
		repository.packageEntities(path);
	}
	
	public void extractEntities(Path path) {
		repository.extractEntities(path);
	}
	
	public String getName() {
		return name;
	}
	
}
