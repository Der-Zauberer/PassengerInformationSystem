package eu.derzauberer.pis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.model.NameEntity;
import eu.derzauberer.pis.repositories.Repository;

public class SearchTree<T extends Entity<T> & NameEntity> {
	
	private final Map<String, String> originalNames = new HashMap<>();
	private final Map<String, List<String>> entries = new HashMap<>();
	private final Repository<T> repository;
	
	public SearchTree(Repository<T> repository) {
		this.repository = repository;
	}
	
	public void add(T entity) {
		for (String searchString : getSearchStrings(entity.getName())) {
			for (int i = 0; i < searchString.length(); i++) {
				final String subString = searchString.substring(0, i + 1);
				List<String> results;
				if ((results = entries.get(subString)) == null) {
					results = new ArrayList<>();
					entries.put(subString, results);
				}
				results.add(entity.getId());
			}
		}
		originalNames.put(entity.getId(), entity.getName());
	}
	
	public void remove(T entity) {
		removeById(entity.getId());
	}
	
	public void removeById(String id) {
		for (String searchString : getSearchStrings(originalNames.get(id))) {
			for (int i = 0; i < searchString.length(); i++) {
				final String subString = searchString.substring(0, i + 1);
				List<String> results;
				if ((results = entries.get(subString)) != null) {
					results.remove(id);
					if (results.isEmpty()) {
						entries.remove(subString);
					}
				}
			}
		}
		originalNames.remove(id);
	}
	
	
	public List<T> searchByName(String search) {
		final List<T> results = new ArrayList<>();
		final List<String> resultIds;
		if ((resultIds = entries.get(normalizeSearchString(search).replaceAll("\\s", ""))) == null) return results;
		for (String id : resultIds) {
			repository.getById(id).ifPresent(results::add);
		}
		return results;
	}
	
	private String normalizeSearchString(String string) {
		return string.toLowerCase()
				.replaceAll("-{1}", " ")
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
