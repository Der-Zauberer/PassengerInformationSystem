package eu.derzauberer.pis.controller.api;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
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
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/routes")
public class RouteController {
	
	@Autowired
	private RouteService routeService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public ListDto<Route> getRoutes(
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		return new ListDto<>(routeService, offset, limit == -1 ? routeService.size() : limit);
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
	
	@PostMapping("/import")
	public String importStations(@RequestBody String content) {
		routeService.importEntities(content);
		return "Successful imported!";
	}
	
	@GetMapping("/export")
	public Object importStations(@RequestParam(name = "download", defaultValue = "false") boolean donwload, Model model, HttpServletResponse response) throws IOException {
		if (donwload) {
			final String content = routeService.exportEntities();
			response.setContentType("application/octet-stream");
			final String headerKey = "Content-Disposition";
			final String headerValue = "attachment; filename = " + routeService.getName() + ".json";
			response.setHeader(headerKey, headerValue);
			final ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(content.getBytes("UTF-8"));
			outputStream.close();
			return null;
		} else {
			return routeService.getList();
		}
	}
	
	private ResponseStatusException getNotFoundException(String id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "Route with id " + id + " does not exist!");
	}

}
