package eu.derzauberer.pis.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.derzauberer.pis.model.TrainType;
import eu.derzauberer.pis.service.TypeService;

@RestController
@RequestMapping("/api/type")
public class ApiTypeController {
	
	@Autowired
	private TypeService typeService;

	@GetMapping("{id}")
	public TrainType getTrainType(@PathVariable("id") String id) {
		return typeService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	
}
