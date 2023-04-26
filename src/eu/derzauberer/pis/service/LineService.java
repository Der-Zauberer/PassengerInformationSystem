package eu.derzauberer.pis.service;

import org.springframework.stereotype.Service;

import eu.derzauberer.pis.model.LineSceduled;
import eu.derzauberer.pis.util.EntityService;
import eu.derzauberer.pis.util.FileRepository;

@Service
public class LineService extends EntityService<LineSceduled> {

	public LineService() {
		super("lines", new FileRepository<>("lines", LineSceduled.class));
	}

}
