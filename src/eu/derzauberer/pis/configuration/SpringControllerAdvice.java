package eu.derzauberer.pis.configuration;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class SpringControllerAdvice {

	@InitBinder
	public void initBinder(WebDataBinder binder) {  
	    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	private ResponseStatusException handleIllegalArgumentException(IllegalArgumentException exception) {
		return new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
	}
	
	@ExceptionHandler(value = { MaxUploadSizeExceededException.class })
	public void handleConflict(RuntimeException exception) {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

}
