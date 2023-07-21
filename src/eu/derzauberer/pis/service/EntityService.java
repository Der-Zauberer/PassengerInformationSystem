package eu.derzauberer.pis.service;

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
	
	public List<T> getRange(int beginn, int end) {
		return repository.getRange(beginn, end);
	}
	
	public int size() {
		return repository.size();
	}
	
	public boolean isEmpty() {
		return repository.isEmpty();
	}
	
	public String packageEntities() {
		return repository.packageEntities();
	}
	
	public void extractEntities(String content) {
		repository.extractEntities(content);
	}
	
}
