package eu.derzauberer.pis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.model.NameEntity;
import eu.derzauberer.pis.repositories.EntityRepository;

public abstract class EntityService<T extends Entity<T> & NameEntity> extends Service {
	
	private final EntityRepository<T> repository;
	
	private List<Consumer<T>> onAdd = new ArrayList<>();
	private List<Consumer<String>> onRemove = new ArrayList<>();

	public EntityService(EntityRepository<T> repository) {
		super(repository.getName());
		this.repository = repository;
	}
	
	public void add(T entity) {
		if (entity.getId() == null || entity.getId().isEmpty()) {
			throw new IllegalArgumentException("Entity id must be not null and not empty!");
		}
		repository.add(entity);
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
	
	public abstract List<T> search(String search);
	
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
