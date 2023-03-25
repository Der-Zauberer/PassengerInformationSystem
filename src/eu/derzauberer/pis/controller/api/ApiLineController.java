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
import eu.derzauberer.pis.model.Line;
import eu.derzauberer.pis.service.LineService;

@RestController
@RequestMapping("/api/lines")
public class ApiLineController {
	
	@Autowired
	private LineService lineService;
	
	@Autowired
	private ModelMapper modelMapper;
	
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
	
	@PostMapping
	public Line setLine(Line line) {
		lineService.add(line);
		return line;
	}
	
	@PutMapping
	public Line updateLine(Line line) {
		final Line existingLine = lineService.getById(line.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		modelMapper.map(line, existingLine);
		return existingLine;
	}
	
	@DeleteMapping("{id}")
	public void deleteLine(@PathVariable("id") String id) {
		if (!lineService.removeById(id)) new ResponseStatusException(HttpStatus.NOT_FOUND);
	}

}
