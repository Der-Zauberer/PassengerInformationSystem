package eu.derzauberer.pis.controller.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.derzauberer.pis.service.RouteService;
import eu.derzauberer.pis.structure.model.Route;
import eu.derzauberer.pis.util.NotFoundException;
import eu.derzauberer.pis.util.Result;
import eu.derzauberer.pis.util.ResultListDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/routes")
public class RouteController {
	
	@Autowired
	private RouteService routeService;
	
	@GetMapping
	public ResultListDto<Route> getRoutes(
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Result<Route> result = hasSearch ? routeService.search(search) : routeService;
		return result.getList(offset, limit == -1 ? result.size() : limit);
	}
	
	@GetMapping("{id}")
	public Route getRoute(@PathVariable("id") String id) {
		return routeService.getById(id).orElseThrow(() -> new NotFoundException("Route", id));
	}
	
	@PostMapping
	public Route setRoute(@RequestBody Route operator) {
		routeService.save(operator);
		return operator;
	}
	
	@DeleteMapping("{id}")
	public void deleteRoute(@PathVariable("id") String id) {
		if (!routeService.removeById(id)) throw new NotFoundException("Route", id);
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
			return routeService.getAll();
		}
	}

}
