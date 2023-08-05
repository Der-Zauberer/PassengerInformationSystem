package eu.derzauberer.pis.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class SpringControllerAdviceException {

	@ExceptionHandler(value = { MaxUploadSizeExceededException.class })
	public void handleConflict(RuntimeException exception) {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
	}
	
}
