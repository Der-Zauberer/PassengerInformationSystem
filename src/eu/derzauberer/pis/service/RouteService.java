package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;

import eu.derzauberer.pis.model.Route;
import eu.derzauberer.pis.repositories.Repository;

public class RouteService extends EntityService<Route> {

	@Autowired
	public RouteService(Repository<Route> repository) {
		super(repository);
	}

}
