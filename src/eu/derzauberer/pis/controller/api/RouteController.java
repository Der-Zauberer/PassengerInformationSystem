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
import eu.derzauberer.pis.model.Route;
import eu.derzauberer.pis.service.RouteService;

@RestController
@RequestMapping("/api/routes")
public class RouteController {
	
	@Autowired
	private RouteService routeService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public ListDto<Route> getRoutes(
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset
			) {
		return new ListDto<>(routeService.getList(), limit == -1 ? routeService.size() : limit, offset);
	}
	
	@GetMapping("{id}")
	public Route getRoute(@PathVariable("id") String id) {
		return routeService.getById(id).orElseThrow(() -> getNotFoundException(id));
	}
	
	@PostMapping
	public Route setRoute(@RequestBody Route operator) {
		routeService.add(operator);
		return operator;
	}
	
	@PutMapping
	public Route updateRoute(@RequestBody Route operator) {
		final Route existingRoute = routeService.getById(operator.getId()).orElseThrow(() -> getNotFoundException(operator.getId()));
		modelMapper.map(operator, existingRoute);
		routeService.add(existingRoute);
		return existingRoute;
	}
	
	@DeleteMapping("{id}")
	public void deleteRoute(@PathVariable("id") String id) {
		if (!routeService.removeById(id)) throw getNotFoundException(id);
	}
	
	private ResponseStatusException getNotFoundException(String id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "Route with id " + id + " does not exist!");
	}

}
