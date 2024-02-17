package eu.derzauberer.pis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.RouteModel;
import eu.derzauberer.pis.persistence.EntityRepository;
import eu.derzauberer.pis.persistence.Lazy;
import eu.derzauberer.pis.persistence.SearchIndex;

@Service
public class RouteService extends EntityService<RouteModel> {

	private final SearchIndex<RouteModel> searchComponent;
	
	@Autowired
	public RouteService(EntityRepository<RouteModel> repository) {
		super(repository);
		searchComponent = new SearchIndex<>(this);
	}
	
	@Override
	public List<Lazy<RouteModel>> search(String search) {
		return searchComponent.search(search);
	}
	
	public String createRouteId() {
		return String.format("%08x", Long.valueOf(System.nanoTime()).toString().hashCode());
	}

}
