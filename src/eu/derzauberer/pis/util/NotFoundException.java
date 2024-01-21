package eu.derzauberer.pis.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
	
	public NotFoundException(String type, Object id) {
		super(type + "with id " + id + " does not exist!");
	}

}
