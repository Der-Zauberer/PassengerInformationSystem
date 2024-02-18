package eu.derzauberer.pis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.RouteModel;
import eu.derzauberer.pis.persistence.EntityRepository;

@Service
public class RouteService extends EntityService<RouteModel> {
	
	@Autowired
	public RouteService(EntityRepository<RouteModel> repository) {
		super(repository);	
	}
		
	public String createRouteId() {
		return String.format("%08x", Long.valueOf(System.nanoTime()).toString().hashCode());
	}

}
