package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.Route;
import eu.derzauberer.pis.repositories.Repository;

@Service
public class RouteService extends EntityService<Route> {

	@Autowired
	public RouteService(Repository<Route> repository) {
		super(repository);
	}
	
	public String createRouteId() {
		return String.format("%08x", Long.valueOf(System.nanoTime()).toString().hashCode());
	}

}
