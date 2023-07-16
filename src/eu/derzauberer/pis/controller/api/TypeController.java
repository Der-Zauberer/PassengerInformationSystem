package eu.derzauberer.pis.controller.api;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.derzauberer.pis.dto.ListDto;
import eu.derzauberer.pis.model.TrainType;
import eu.derzauberer.pis.service.TypeService;

@RestController
@RequestMapping("/api/types")
public class TypeController {
	
	@Autowired
	private TypeService typeService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public ListDto<TrainType> getTrainTypes(
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		return new ListDto<>(typeService, offset, limit == -1 ? typeService.size() : limit);
	}

	@GetMapping("{id}")
	public TrainType getTrainType(@PathVariable("id") String id) {
		return typeService.getById(id).orElseThrow(() -> getNotFoundException(id));
	}
	
	@PostMapping
	public TrainType setType(@RequestBody TrainType type) {
		typeService.add(type);
		return type;
	}
	
	@PutMapping
	public TrainType updateType(@RequestBody TrainType type) {
		final TrainType existingType = typeService.getById(type.getId()).orElseThrow(() -> getNotFoundException(type.getId()));
		modelMapper.map(type, existingType);
		typeService.add(existingType);
		return existingType;
	}
	
	@DeleteMapping("{id}")
	public void deleteType(@PathVariable("id") String id) {
		if (!typeService.removeById(id)) throw getNotFoundException(id);
	}
	
	private ResponseStatusException getNotFoundException(String id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "Type with id " + id + " does not exist!");
	}
	
}
