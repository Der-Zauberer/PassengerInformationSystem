package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.persistence.Repository;
import eu.derzauberer.pis.persistence.SearchIndex;
import eu.derzauberer.pis.structure.model.RouteModel;
import eu.derzauberer.pis.util.Result;

@Service
public class RouteService extends EntityService<RouteModel> {

	private final SearchIndex<RouteModel> searchComponent;
	
	@Autowired
	public RouteService(Repository<RouteModel> repository) {
		super(repository);
		searchComponent = new SearchIndex<>(this);
	}
	
	@Override
	public Result<RouteModel> search(String search) {
		return searchComponent.search(search);
	}
	
	public String createRouteId() {
		return String.format("%08x", Long.valueOf(System.nanoTime()).toString().hashCode());
	}

}
