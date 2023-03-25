package eu.derzauberer.pis.service;

import eu.derzauberer.pis.model.Line;
import eu.derzauberer.pis.util.FileRepository;
import eu.derzauberer.pis.util.Service;

@org.springframework.stereotype.Service
public class LineService extends Service<Line> {

	public LineService() {
		super("lines", new FileRepository<>("lines", Line.class));
	}

}
