package eu.derzauberer.pis.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
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
	private final SearchIndex searchIndex;
	
	private final Logger LOGGER = LoggerFactory.getLogger(FileEntityRepository.class);
	
	private static String ID_INDEX = "ids";
	private static String SEARCH_INDEX = "search";
	
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
			IdIndex loadedIdIndex;
			if ((loadedIdIndex = indexFileHandler.load(ID_INDEX, IdIndex.class).orElse(null)) == null) {
				LOGGER.info("Indexing ids in {}", name);
				idIndex = new IdIndex(new HashMap<>());
				entities.stream().map(Lazy::get).forEach(entity -> entity.getSecondaryIds().forEach(id -> idIndex.entries().put(id, entity.getId())));
			} else {
				idIndex = loadedIdIndex;
			}
		} else {
			idIndex = null;
		}

		
		if (Namable.class.isAssignableFrom(type)) {
			SearchIndex loadedSearchIndex;
			if ((loadedSearchIndex = indexFileHandler.load(SEARCH_INDEX, SearchIndex.class).orElse(null)) == null) {
				LOGGER.info("Indexing names in {}", name);
				searchIndex = new SearchIndex(new HashMap<>());
				entities.stream().map(Lazy::get).forEach(entity -> updateSerachIndex((Namable) entity, true));
				indexFileHandler.save(ID_INDEX, idIndex);
			} else {
				searchIndex = loadedSearchIndex;
			}
		} else {
			searchIndex = null;
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
		if (idIndex == null) throw new UnsupportedOperationException("Id indexing is not enabled for " + name + " repository!");
		if (id == null) return Optional.empty();
		return Optional.ofNullable(idIndex.entries().get(id))
			.map(ids::get)
			.or(() -> Optional.ofNullable(ids.get(id)))
			.map(Lazy::get)
			.map(T::copy);
	}
	
	@Override
	public List<Lazy<T>> search(String string) {
		if (searchIndex == null) throw new UnsupportedOperationException("Type " + type.getSimpleName() + " must implement Namable to enable search!");
		final String normalizedSearchString = getNormalizedSearchString(string);
		final Optional<T> exactEntity = idIndex != null ? getByIdOrSecondaryId(normalizedSearchString) : getById(normalizedSearchString);
		final List<T> searchResult = new ArrayList<>();
		if (searchIndex.entries().get(normalizedSearchString) != null) {
			searchIndex.entries().get(normalizedSearchString).stream().map(ids::get).map(Lazy::get).map(T::copy).forEach(searchResult::add);
		}
		Collections.sort(searchResult, (entity1, entity2) -> ((Namable) entity1).compareSearchTo(normalizedSearchString, (Namable) entity2));
		exactEntity.ifPresent(entity -> {
			searchResult.remove(entity);
			searchResult.add(0, entity);
		});
		return searchResult.stream().map(entity -> new Lazy<>(entity.getId(), () -> entity)).toList();
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
	public SortedSet<Lazy<T>> getAll() {
		return Collections.unmodifiableSortedSet(entities);
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
		
		final Lazy<T> oldEntityReference = ids.get(entity.getId());
		final T oldEntity = Lazy.getOrNull(oldEntityReference);
		
		final T copy = entity.copy();
		final Lazy<T> reference = new Lazy<>(entity.getId(), eagerLoading ? () -> copy : () -> entityfileHandler.load(copy.getId()).orElse(null));
		
		entityfileHandler.save(copy.getId(), copy);
		ids.put(copy.getId(), reference);
		if (oldEntityReference != null) entities.remove(oldEntityReference);
		entities.add(reference);
		
		if (idIndex != null) {
			if (oldEntity != null) oldEntity.getSecondaryIds().forEach(idIndex.entries()::remove);
			entity.getSecondaryIds().forEach(id -> idIndex.entries().put(id, copy.getId()));
			indexFileHandler.save(ID_INDEX, idIndex);
		}
		if (searchIndex != null && (oldEntity == null || !((Namable) oldEntity).getName().equals(((Namable) copy).getName()))) {
			updateSerachIndex((Namable) copy, true);
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
		
		if (idIndex != null) entity.getSecondaryIds().forEach(idIndex.entries()::remove);
		if (searchIndex != null) updateSerachIndex((Namable) entity, false);
		
		return true;
	}
	
	private void updateSerachIndex(Namable namable, boolean add) {
		final String normalizedSearchString = getNormalizedSearchString(namable.getName());
		
		final List<String> searchStrings = new ArrayList<>();
		final List<Integer> spaceIndices = new ArrayList<>();
		spaceIndices.add(0);
		for (int i = 0; i < normalizedSearchString.length(); i++) {
			if (normalizedSearchString.charAt(i) == ' ') spaceIndices.add(i + 1);
		}
		for (int i = 0; i < spaceIndices.size(); i++) {
			final String subString = normalizedSearchString.substring(spaceIndices.get(i));
			for (int j = 0; j < subString.length(); j++) {
				searchStrings.add(subString.substring(0, j + 1));
			}
		}
		
		for (String seachString : searchStrings) {
			Set<String> results = null;
			if (add) {
				if ((results = searchIndex.entries().get(seachString)) == null) {
					results = new HashSet<>();
					searchIndex.entries().put(seachString, results);
				}
				results.add(namable.getId());
			} else {
				if ((results = searchIndex.entries().get(seachString)) != null) {
					results.remove(namable.getId());
					if (results.isEmpty()) {
						searchIndex.entries().remove(seachString);
					}
				}
			}
		}
		indexFileHandler.save(SEARCH_INDEX, searchIndex);
	}
	
	private String getNormalizedSearchString(String string) {
		return StringUtils.stripAccents(string.toLowerCase().replaceAll("-{1}", " "))
				.replaceAll("[^A-Za-z0-9\\r\\n\\t\\f\\v ]", "")
				.replaceAll("\\s{2,}", " ");
	}
	
	public record IdIndex(Map<String, String> entries) {}
	public record SearchIndex(Map<String, Set<String>> entries) {}

}
