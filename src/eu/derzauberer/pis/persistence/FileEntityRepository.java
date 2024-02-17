package eu.derzauberer.pis.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.util.ProgressStatus;

public class FileEntityRepository<T extends Entity<T>> implements EntityRepository<T> {
	
	private final String name;
	private final Class<T> type;
	private final boolean eagerLoading;
	
	private final Map<String, Lazy<T>> ids;
	private final SortedSet<Lazy<T>> entities;
	
	private final JsonFileHandler<T> entityfileHandler;
	private final JsonFileHandler<Object> indexFileHandler;
	
	private final IdIndex idIndex;
	
	private final Logger LOGGER = LoggerFactory.getLogger(FileEntityRepository.class);
	
	private static String ID_INDEX = "ids";
	
	public FileEntityRepository(String name, Class<T> type, boolean eagerLoading, boolean idIndexing) {
		this.name = name;
		this.type = type;
		this.eagerLoading = eagerLoading;
		
		ids = new HashMap<>();
		this.entities = new TreeSet<>();
		
		this.entityfileHandler = new JsonFileHandler<>(name, name + "/entities", type, LOGGER);
		this.indexFileHandler = new JsonFileHandler<>("id index", name, Object.class, LOGGER);
		
		final ProgressStatus progress = new ProgressStatus(name, entityfileHandler.size());
		if (eagerLoading) {
			entityfileHandler.stream()
				.map(Lazy::get)
				.map(entity -> {progress.count(); return entity;})
				.forEach(entity -> {
					final Lazy<T> reference = new Lazy<>(entity.getId(), () -> entity);
					ids.put(entity.getId(), reference);
					entities.add(reference);
				});
		} else {
			entityfileHandler.stream().forEach(reference -> {
				ids.put(reference.getId(), reference);
				entities.add(reference);
			});
		}
		
		if (idIndexing) {
			final Supplier<IdIndex> loadNewIndex = () -> {
				final IdIndex index = new IdIndex(new HashMap<>());
				entities.stream().map(Lazy::get).forEach(entity -> entity.getSecondaryIds().forEach(id -> index.entries().put(id, entity.getId())));
				return index;
			};
			idIndex = eagerLoading ? loadNewIndex.get() : indexFileHandler.load(ID_INDEX, IdIndex.class).orElseGet(() -> loadNewIndex.get());
			indexFileHandler.save(ID_INDEX, idIndex);
		} else {
			idIndex = null;
		}
		
		LOGGER.info("Loaded {} {}", entities.size(), name);
	}
	
	public FileEntityRepository(String name, Class<T> type, boolean eagerLoading) {
		this(name, type, eagerLoading, false);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<T> getType() {
		return type;
	}

	@Override
	public Optional<T> getById(String id) {
		if (id == null) return Optional.empty();
		return Optional.ofNullable(ids.get(id)).map(Lazy::get).map(T::copy);
	}
	
	@Override
	public Optional<T> getByIdOrSecondaryId(String id) {
		if (id == null) return Optional.empty();
		return Optional.ofNullable(idIndex.entries().get(id))
			.map(ids::get)
			.or(() -> Optional.ofNullable(ids.get(id)))
			.map(Lazy::get)
			.map(T::copy);
	}
	
	@Override
	public boolean containsById(String id) {
		Objects.requireNonNull(id);
		return ids.containsKey(id);
	}
	
	@Override
	public int size() {
		return entities.size();
	}
	
	@Override
	public Collection<Lazy<T>> getAll() {
		return entities;
	}
	
	@Override
	public void save(T entity) {
		Objects.requireNonNull(entity);
		if (entity.getId() == null || entity.getId().isEmpty()) {
			throw new IllegalArgumentException("Entity id must not be null");
		} else if (entity instanceof Namable && (((Namable) entity).getName() == null || ((Namable) entity).getName().isEmpty())) {
			throw new IllegalArgumentException("Entity name must not be null");
		} else if (idIndex != null) {
			if (entity.getSecondaryIds().stream().anyMatch(ids::containsKey)
					|| entity.getSecondaryIds().stream().anyMatch(id -> idIndex.entries.containsKey(id) 
							&& !idIndex.entries.get(id).equals(entity.getId()))) {
				throw new IllegalArgumentException("Entity's secondary id must not be unique");
			}
		}
		
		final T oldEntity = getById(entity.getId()).orElse(null);
		
		final T copy = entity.copy();
		final Lazy<T> reference = new Lazy<>(entity.getId(), eagerLoading ? () -> copy : () -> entityfileHandler.load(copy.getId()).orElse(null));
		
		entityfileHandler.save(copy.getId(), copy);
		ids.put(copy.getId(), reference);
		entities.add(reference);
		
		if (idIndex != null) {
			if (oldEntity != null) oldEntity.getSecondaryIds().forEach(idIndex.entries()::remove);
			entity.getSecondaryIds().forEach(id -> idIndex.entries().put(id, copy.getId()));
			indexFileHandler.save(ID_INDEX, idIndex);
		}
	}
	
	@Override
	public boolean removeById(String id) {
		Objects.requireNonNull(id);
		final Lazy<T> reference = ids.get(id);
		final T entity = Lazy.getOrNull(reference);
		if (entity == null) return false;
		
		entityfileHandler.delete(entity.getId());
		ids.remove(id);
		entities.remove(reference);
		
		entity.getSecondaryIds().forEach(idIndex.entries()::remove);
		
		return true;
	}
	
	public record IdIndex(Map<String, String> entries) {}

}
