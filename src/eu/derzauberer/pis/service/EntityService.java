package eu.derzauberer.pis.service;

import java.util.List;
import java.util.Optional;

import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.repositories.EntityRepository;

public abstract class EntityService<T extends Entity<T>> {
	
	private final EntityRepository<T> repository;

	public EntityService(EntityRepository<T> repository) {
		this.repository = repository;
	}
	
	public String getName() {
		return repository.getName();
	}
	
	public void add(T entity) {
		if (entity.getId() == null || entity.getId().isEmpty()) {
			throw new IllegalArgumentException("Entity id must be not null and not empty!");
		}
		repository.add(entity);
	}
	
	public boolean removeById(String id) {
		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException("Id must be not null and not empty!");
		}
		return repository.removeById(id);
	}
	
	public boolean containsById(String id) {
		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException("Id must be not null and not empty!");
		}
		return repository.containsById(id);
	}
	
	public Optional<T> getById(String id) {
		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException("Id must be not null and not empty!");
		}
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
	
	public String exportEntities() {
		return repository.exportEntities();
	}
	
	public void importEntities(String content) {
		repository.importEntities(content);
	}
	
}
