package eu.derzauberer.pis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.model.NameEntity;
import eu.derzauberer.pis.repository.EntityRepository;
import eu.derzauberer.pis.util.RemoveEvent;
import eu.derzauberer.pis.util.Result;
import eu.derzauberer.pis.util.SaveEvent;

public abstract class EntityService<T extends Entity<T> & NameEntity> implements Result<T> {
	
	private final EntityRepository<T> repository;
	private List<Consumer<SaveEvent<T>>> onSave = new ArrayList<>();
	private List<Consumer<RemoveEvent<T>>> onRemove = new ArrayList<>();

	public EntityService(EntityRepository<T> repository) {
		this.repository = repository;
	}
	
	public String getName() {
		return repository.getName();
	}
	
	public void save(T entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity must not be null!");
		}
		if (entity.getId() == null || entity.getId().isEmpty()) {
			throw new IllegalArgumentException("Entity id must not be null or empty!");
		}
		if (entity.getName() == null || entity.getName().isEmpty()) {
			throw new IllegalArgumentException("Entity name must not be null or empty!");
		}
		final Optional<T> oldEntity = repository.getById(entity.getId());
		repository.save(entity);
		final SaveEvent<T> saveEvent = new SaveEvent<>(oldEntity, entity);
		onSave.forEach(consumer -> consumer.accept(saveEvent));
	}
	
	public boolean removeById(String id) {
		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException("Id must not be null or empty!");
		}
		final Optional<T> oldEntity = repository.getById(id);
		final boolean removed = repository.removeById(id);
		if (removed) {
			final RemoveEvent<T> removeEvent = new RemoveEvent<>(oldEntity.orElse(null));
			onRemove.forEach(consumer -> consumer.accept(removeEvent));
		}
		return removed;
	}
	
	public boolean containsById(String id) {
		return repository.containsById(id);
	}
	
	public Optional<T> getById(String id) {
		return repository.getById(id);
	}
	
	public abstract Result<T> search(String search);
	
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
	
	public void addOnSave(Consumer<SaveEvent<T>> event) {
		onSave.add(event);
	}
	
	public void addOnRemove(Consumer<RemoveEvent<T>> event) {
		onRemove.add(event);
	}
	
}
