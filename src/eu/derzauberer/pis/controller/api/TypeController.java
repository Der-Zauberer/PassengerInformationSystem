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
import eu.derzauberer.pis.model.Type;
import eu.derzauberer.pis.service.TypeService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/types")
public class TypeController {
	
	@Autowired
	private TypeService typeService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public ListDto<Type> getTrainTypes(
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		return new ListDto<>(typeService, offset, limit == -1 ? typeService.size() : limit);
	}

	@GetMapping("{id}")
	public Type getTrainType(@PathVariable("id") String id) {
		return typeService.getById(id).orElseThrow(() -> getNotFoundException(id));
	}
	
	@PostMapping
	public Type setType(@RequestBody Type type) {
		typeService.add(type);
		return type;
	}
	
	@PutMapping
	public Type updateType(@RequestBody Type type) {
		final Type existingType = typeService.getById(type.getId()).orElseThrow(() -> getNotFoundException(type.getId()));
		modelMapper.map(type, existingType);
		typeService.add(existingType);
		return existingType;
	}
	
	@DeleteMapping("{id}")
	public void deleteType(@PathVariable("id") String id) {
		if (!typeService.removeById(id)) throw getNotFoundException(id);
	}
	
	@PostMapping("/import")
	public String importStations(@RequestBody String content) {
		typeService.importEntities(content);
		return "Successful imported!";
	}
	
	@GetMapping("/export")
	public Object importStations(@RequestParam(name = "download", defaultValue = "false") boolean donwload, Model model, HttpServletResponse response) throws IOException {
		if (donwload) {
			final String content = typeService.exportEntities();
			response.setContentType("application/octet-stream");
			final String headerKey = "Content-Disposition";
			final String headerValue = "attachment; filename = " + typeService.getName() + ".json";
			response.setHeader(headerKey, headerValue);
			final ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(content.getBytes("UTF-8"));
			outputStream.close();
			return null;
		} else {
			return typeService.getList();
		}
	}
	
	private ResponseStatusException getNotFoundException(String id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "Type with id " + id + " does not exist!");
	}
	
}
