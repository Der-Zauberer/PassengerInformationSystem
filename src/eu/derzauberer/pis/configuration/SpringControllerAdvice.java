package eu.derzauberer.pis.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class SpringControllerAdvice {

	@ExceptionHandler(IllegalArgumentException.class)
	private ResponseStatusException handleIllegalArgumentException(IllegalArgumentException exception) {
		return new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

}
