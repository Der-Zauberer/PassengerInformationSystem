package eu.derzauberer.pis.util;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.repositories.Repository;

public abstract class EntityService<T extends Entity<T>> {
	
	private final String name;
	private final Repository<T> repository;
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(EntityService.class.getSimpleName());

	public EntityService(String name, Repository<T> repository) {
		this.name = name;
		this.repository = repository;
	}
	
	public void add(T entity) {
		repository.add(entity);
	}
	
	public boolean removeById(String id) {
		return repository.removeById(id);
	}
	
	public boolean containsById(String id) {
		return repository.containsById(id);
	}
	
	public Optional<T> getById(String id) {
		return repository.getById(id);
	}
	
	public List<T> getList() {
		return repository.getList();
	}
	
	public int size() {
		return repository.size();
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
	
	public Repository<T> getRepository() {
		return repository;
	}
	
}
