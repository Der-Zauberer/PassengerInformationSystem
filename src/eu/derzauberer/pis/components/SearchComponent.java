package eu.derzauberer.pis.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.model.NameEntity;
import eu.derzauberer.pis.model.SearchIndex;
import eu.derzauberer.pis.service.EntityService;
import eu.derzauberer.pis.util.SearchComparator;

public class SearchComponent<T extends Entity<T> & NameEntity> extends Component<EntityService<T>, SearchIndex> {

	private final SearchIndex index;
	private final SearchComparator<T> comparator;
	private boolean generateIndex = false;
	
	public SearchComponent(EntityService<T> service) {
		this(service, null);
	}
	
	public SearchComponent(EntityService<T> service, SearchComparator<T> comparator) {
		super("search", service, LoggerFactory.getLogger(SearchComponent.class));
		this.comparator = comparator;
		index = loadAsOptional(SearchIndex.class).orElseGet(() -> {
			generateIndex = true;
			return new SearchIndex(new HashMap<>(), new HashMap<>());
		});
		if (generateIndex) {
			getService().getList().forEach(this::add);
			save(index);
		}
		getService().addOnAdd(entity -> {
			final String originalName = index.getOriginalNames().get(entity.getId());
			if (originalName != null && originalName.equals(entity.getName())) return;
			removeById(entity.getId());
			add(entity);
			save(index);
		});
		getService().addOnRemove(id -> {
			removeById(id);
			save(index);
		});
	}
	
	public List<T> search(String search) {
		Objects.requireNonNull(search);
		final List<T> results = new ArrayList<>();
		final List<String> resultIds;
		if ((resultIds = index.getEntries().get(normalizeSearchString(search).replaceAll("\\s", ""))) != null) {
			for (String id : resultIds) {
				getService().getById(id).ifPresent(results::add);
			}
			if (comparator != null) Collections.sort(results, (o1, o2) -> comparator.compare(search, o1, o2));
		}
		getService().getById(search).ifPresent(entity -> {
			results.remove(entity);
			results.add(0, entity);
		});
		return results;
	}
	
	private void add(T entity) {
		for (String searchString : getSearchStrings(entity.getName())) {
			for (int i = 0; i < searchString.length(); i++) {
				final String subString = searchString.substring(0, i + 1);
				List<String> results;
				if ((results = index.getEntries().get(subString)) == null) {
					results = new ArrayList<>();
					index.getEntries().put(subString, results);
				}
				results.add(entity.getId());
				Collections.sort(results);
			}
		}
		index.getOriginalNames().put(entity.getId(), entity.getName());
	}
	
	private void removeById(String id) {
		if (index.getOriginalNames().get(id) == null) return;
		for (String searchString : getSearchStrings(index.getOriginalNames().get(id))) {
			for (int i = 0; i < searchString.length(); i++) {
				final String subString = searchString.substring(0, i + 1);
				List<String> results;
				if ((results = index.getEntries().get(subString)) != null) {
					results.remove(id);
					if (results.isEmpty()) {
						index.getEntries().remove(subString);
					}
				}
			}
		}
		index.getOriginalNames().remove(id);
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

}
