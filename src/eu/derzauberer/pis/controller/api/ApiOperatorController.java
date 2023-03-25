package eu.derzauberer.pis.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.derzauberer.pis.model.TrainOperator;
import eu.derzauberer.pis.service.OperatorService;

@RestController
@RequestMapping("/api/operator")
public class ApiOperatorController {
	
	@Autowired
	private OperatorService operatorService;
	
	@GetMapping("{id}")
	public TrainOperator getOperator(@PathVariable("id") String id) {
		return operatorService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

}
