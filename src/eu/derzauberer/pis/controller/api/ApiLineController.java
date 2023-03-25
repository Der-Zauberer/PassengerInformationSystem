package eu.derzauberer.pis.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.derzauberer.pis.dto.ListDto;
import eu.derzauberer.pis.model.Line;
import eu.derzauberer.pis.service.LineService;

@RestController
@RequestMapping("/api/lines")
public class ApiLineController {
	
	@Autowired
	private LineService lineService;
	
	@GetMapping
	public ListDto<Line> getLines(
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		return new ListDto<>(offset, limit , lineService.getList());
	}
	
	@GetMapping("{id}")
	public Line getLine(@PathVariable("id") String id) {
		return lineService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

}
