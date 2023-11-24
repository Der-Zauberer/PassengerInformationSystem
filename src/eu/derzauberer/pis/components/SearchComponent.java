package eu.derzauberer.pis.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.model.NameEntity;
import eu.derzauberer.pis.service.EntityService;
import eu.derzauberer.pis.util.Result;
import eu.derzauberer.pis.util.ResultList;
import eu.derzauberer.pis.util.SearchComparator;

public class SearchComponent<T extends Entity<T> & NameEntity> extends Component<EntityService<T>, SearchComponent.Index> {

	private final Index index;
	private final SearchComparator<T> comparator;
	private boolean generateIndex = false;
	
	public SearchComponent(EntityService<T> service) {
		this(service, null);
	}
	
	public SearchComponent(EntityService<T> service, SearchComparator<T> comparator) {
		super("search", service, LoggerFactory.getLogger(SearchComponent.class));
		this.comparator = comparator;
		index = loadAsOptional(Index.class).orElseGet(() -> {
			generateIndex = true;
			return new Index(new HashMap<>());
		});
		if (generateIndex) {
			getService().getAll().forEach(this::add);
			save(index);
		}
		getService().addOnSave(event -> {
			final boolean newIndexRequired = event.oldEntity().filter(oldEntity -> oldEntity.getName().equals(event.newEntity().getName())).isEmpty();
			if (newIndexRequired) {
				event.oldEntity().ifPresent(this::remove);
				add(event.newEntity());
				save(index);
			}
		});
		getService().addOnRemove(event -> {
			remove(event.oldEntity());
			save(index);
		});
	}
	
	public Result<T> search(String search) {
		Objects.requireNonNull(search);
		final List<T> results = new ArrayList<>();
		final Set<String> resultIds;
		if ((resultIds = index.entries().get(normalizeSearchString(search).replaceAll("\\s", ""))) != null) {
			for (String id : resultIds) {
				getService().getById(id).filter(entity -> !entity.getId().equals(search)).ifPresent(results::add);
			}
			if (comparator != null) {
				Collections.sort(results, (o1, o2) -> comparator.compare(search, o1, o2));
			} else {
				Collections.sort(results, (o1, o2) -> o1.getName().compareTo(o2.getName()));
			}
		}
		getService().getById(search).ifPresent(entity -> {
			results.add(0, entity);
		});
		return new ResultList<>(results);
	}
	
	private void add(T entity) {
		for (String searchString : getSearchStrings(entity.getName())) {
			for (int i = 0; i < searchString.length(); i++) {
				final String subString = searchString.substring(0, i + 1);
				Set<String> results;
				if ((results = index.entries().get(subString)) == null) {
					results = new HashSet<>();
					index.entries().put(subString, results);
				}
				results.add(entity.getId());
			}
		}
	}
	
	private void remove(T entity) {
		for (String searchString : getSearchStrings(entity.getName())) {
			for (int i = 0; i < searchString.length(); i++) {
				final String subString = searchString.substring(0, i + 1);
				Set<String> results;
				if ((results = index.entries().get(subString)) != null) {
					results.remove(entity.getId());
					if (results.isEmpty()) {
						index.entries().remove(subString);
					}
				}
			}
		}
		save(index);
	}
	
	private String normalizeSearchString(String string) {
		return StringUtils.stripAccents(string.toLowerCase().replaceAll("-{1}", " "))
				.replaceAll("[^A-Za-z0-9\\r\\n\\t\\f\\v ]", "")
				.replaceAll("\\s{2,}", " ");
	}
	
	private List<String> getSearchStrings(String string) {
		final String searchString = normalizeSearchString(string);
		final List<String> searchStrings = new ArrayList<>();
		searchStrings.add(searchString.replaceAll("\\s", ""));
		int index = searchString.indexOf(' ');
		while (index >= 0) {
			searchStrings.add(searchString.substring(index).replaceAll("\\s", ""));
		    index = searchString.indexOf(' ', index + 1);
		}
		return searchStrings;
	}
	
	public record Index(Map<String, Set<String>> entries) {}

}
