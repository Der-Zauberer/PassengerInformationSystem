package eu.derzauberer.pis.controller.api;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.derzauberer.pis.dto.ListDto;
import eu.derzauberer.pis.model.TrainOperator;
import eu.derzauberer.pis.service.OperatorService;

@RestController
@RequestMapping("/api/operators")
public class ApiOperatorController {
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public ListDto<TrainOperator> getOperators(
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		return new ListDto<>(offset, limit , operatorService.getList());
	}
	
	@GetMapping("{id}")
	public TrainOperator getOperator(@PathVariable("id") String id) {
		return operatorService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	
	@PostMapping
	public TrainOperator setOperator(TrainOperator operator) {
		operatorService.add(operator);
		return operator;
	}
	
	@PutMapping
	public TrainOperator updateOperator(TrainOperator operator) {
		final TrainOperator existingOperator = operatorService.getById(operator.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		modelMapper.map(operator, existingOperator);
		return existingOperator;
	}
	
	@DeleteMapping("{id}")
	public void deleteOperator(@PathVariable("id") String id) {
		if (!operatorService.removeById(id)) new ResponseStatusException(HttpStatus.NOT_FOUND);
	}

}
