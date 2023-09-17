package eu.derzauberer.pis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.components.SearchComponent;
import eu.derzauberer.pis.model.Route;
import eu.derzauberer.pis.repositories.EntityRepository;

@Service
public class RouteService extends EntityService<Route> {

	private final SearchComponent<Route> searchComponent;
	
	@Autowired
	public RouteService(EntityRepository<Route> repository) {
		super(repository);
		searchComponent = new SearchComponent<>(this);
		getList().forEach(searchComponent::add);
	}
	
	@Override
	public List<Route> search(String search) {
		return searchComponent.search(search);
	}
	
	public String createRouteId() {
		return String.format("%08x", Long.valueOf(System.nanoTime()).toString().hashCode());
	}

}
