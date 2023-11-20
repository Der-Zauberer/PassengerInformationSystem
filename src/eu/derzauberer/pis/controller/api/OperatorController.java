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
import eu.derzauberer.pis.entity.Operator;
import eu.derzauberer.pis.service.OperatorService;
import eu.derzauberer.pis.util.Collectable;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/operators")
public class OperatorController {
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public ListDto<Operator> getOperators(
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Collectable<Operator> collectable = hasSearch ? operatorService.search(search) : operatorService;
		return collectable.getList(offset, limit == -1 ? collectable.size() : limit);
	}
	
	@GetMapping("{id}")
	public Operator getOperator(@PathVariable("id") String id) {
		return operatorService.getById(id).orElseThrow(() -> getNotFoundException(id));
	}
	
	@PostMapping
	public Operator setOperator(@RequestBody Operator operator) {
		operatorService.save(operator);
		return operator;
	}
	
	@PutMapping
	public Operator updateOperator(@RequestBody Operator operator) {
		final Operator existingOperator = operatorService.getById(operator.getId()).orElseThrow(() -> getNotFoundException(operator.getId()));
		modelMapper.map(operator, existingOperator);
		operatorService.save(existingOperator);
		return existingOperator;
	}
	
	@DeleteMapping("{id}")
	public void deleteOperator(@PathVariable("id") String id) {
		if (!operatorService.removeById(id)) throw getNotFoundException(id);
	}
	
	@PostMapping("/import")
	public String importStations(@RequestBody String content) {
		operatorService.importEntities(content);
		return "Successful imported!";
	}
	
	@GetMapping("/export")
	public Object importStations(@RequestParam(name = "download", defaultValue = "false") boolean donwload, Model model, HttpServletResponse response) throws IOException {
		if (donwload) {
			final String content = operatorService.exportEntities();
			response.setContentType("application/octet-stream");
			final String headerKey = "Content-Disposition";
			final String headerValue = "attachment; filename = " + operatorService.getName() + ".json";
			response.setHeader(headerKey, headerValue);
			final ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(content.getBytes("UTF-8"));
			outputStream.close();
			return null;
		} else {
			return operatorService.getAll();
		}
	}
	
	private ResponseStatusException getNotFoundException(String id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "Operator with id " + id + " does not exist!");
	}

}
