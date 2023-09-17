package eu.derzauberer.pis.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.model.NameEntity;
import eu.derzauberer.pis.service.EntityService;

@JsonIncludeProperties({ "originalNames", "entries" })
@JsonPropertyOrder({ "originalNames", "entries" })
public class SearchComponent<T extends Entity<T> & NameEntity> extends Component<EntityService<T>, Object> {

	private final Map<String, String> originalNames = new HashMap<>();
	private final Map<String, List<String>> entries = new HashMap<>();
	
	public SearchComponent(EntityService<T> service) {
		super("search", service, LoggerFactory.getLogger(SearchComponent.class));
		getService().addOnAdd(entity -> { removeById(entity.getId()); add(entity); });
		getService().addOnRemove(this::removeById);
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
	
	public void removeById(String id) {
		if (originalNames.get(id) == null) return;
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
	
	public List<T> search(String search) {
		final List<T> results = new ArrayList<>();
		final List<String> resultIds;
		if ((resultIds = entries.get(normalizeSearchString(search).replaceAll("\\s", ""))) == null) return results;
		for (String id : resultIds) {
			getService().getById(id).ifPresent(results::add);
		}
		return results;
	}

}
