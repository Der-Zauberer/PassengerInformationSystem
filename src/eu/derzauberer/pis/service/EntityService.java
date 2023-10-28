package eu.derzauberer.pis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.model.NameEntity;
import eu.derzauberer.pis.repositories.EntityRepository;
import eu.derzauberer.pis.util.Collectable;

public abstract class EntityService<T extends Entity<T> & NameEntity> implements Collectable<T> {
	
	private final EntityRepository<T> repository;
	private List<Consumer<T>> onAdd = new ArrayList<>();
	private List<Consumer<String>> onRemove = new ArrayList<>();

	public EntityService(EntityRepository<T> repository) {
		this.repository = repository;
	}
	
	public String getName() {
		return repository.getName();
	}
	
	public void save(T entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity must be not null!");
		}
		if (entity.getId() == null || entity.getId().isEmpty()) {
			throw new IllegalArgumentException("Entity id must be not null and not empty!");
		}
		if (entity.getName() == null || entity.getName().isEmpty()) {
			throw new IllegalArgumentException("Entity name must be not null and not empty!");
		}
		repository.save(entity);
		onAdd.forEach(consumer -> consumer.accept(entity));
	}
	
	public boolean removeById(String id) {
		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException("Id must be not null and not empty!");
		}
		onRemove.forEach(consumer -> consumer.accept(id));
		return repository.removeById(id);
	}
	
	public boolean containsById(String id) {
		return repository.containsById(id);
	}
	
	public Optional<T> getById(String id) {
		return repository.getById(id);
	}
	
	public abstract Collectable<T> search(String search);
	
	@Override
	public List<T> getAll() {
		return repository.getAll();
	}
	
	@Override
	public List<T> getRange(int beginn, int end) {
		return repository.getRange(beginn, end);
	}
	
	@Override
	public int size() {
		return repository.size();
	}
	
	@Override
	public boolean isEmpty() {
		return repository.isEmpty();
	}
	
	public String exportEntities() {
		return repository.exportEntities();
	}
	
	public void importEntities(String json) {
		repository.importEntities(json);
	}
	
	public void addOnAdd(Consumer<T> action) {
		onAdd.add(action);
	}
	
	public void addOnRemove(Consumer<String> action) {
		onRemove.add(action);
	}
	
}
