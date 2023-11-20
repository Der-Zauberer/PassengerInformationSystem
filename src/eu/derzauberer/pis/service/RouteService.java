package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.components.SearchComponent;
import eu.derzauberer.pis.model.Route;
import eu.derzauberer.pis.repository.EntityRepository;
import eu.derzauberer.pis.util.Collectable;

@Service
public class RouteService extends EntityService<Route> {

	private final SearchComponent<Route> searchComponent;
	
	@Autowired
	public RouteService(EntityRepository<Route> repository) {
		super(repository);
		searchComponent = new SearchComponent<>(this);
	}
	
	@Override
	public Collectable<Route> search(String search) {
		return searchComponent.search(search);
	}
	
	public String createRouteId() {
		return String.format("%08x", Long.valueOf(System.nanoTime()).toString().hashCode());
	}

}
