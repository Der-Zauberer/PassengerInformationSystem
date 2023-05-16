package eu.derzauberer.pis.service;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.repositories.Repository;

public abstract class EntityService<T extends Entity<T>> {
	
	private final Repository<T> repository;

	public EntityService(Repository<T> repository) {
		this.repository = repository;
	}
	
	public String getName() {
		return repository.getName();
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
	
}
