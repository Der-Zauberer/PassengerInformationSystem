package eu.derzauberer.pis.controller.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.derzauberer.pis.converter.DataConverter;
import eu.derzauberer.pis.converter.FormConverter;
import eu.derzauberer.pis.service.TypeService;
import eu.derzauberer.pis.structure.data.TransportationTypeData;
import eu.derzauberer.pis.structure.form.TransportationTypeForm;
import eu.derzauberer.pis.structure.model.TransportationType;
import eu.derzauberer.pis.util.Result;
import eu.derzauberer.pis.util.ResultListDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/types")
public class TypeController {
	
	@Autowired
	private TypeService typeService;
	
	@Autowired 
	private DataConverter<TransportationType, TransportationTypeData> typeDataConverter;
	
	@Autowired
	private FormConverter<TransportationType, TransportationTypeForm> typeFormConverter;
	
	@GetMapping
	public ResultListDto<TransportationTypeData> getTypes(
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Result<TransportationType> result = hasSearch ? typeService.search(search) : typeService;
		return result.map(typeDataConverter::convert).getList(offset, limit == -1 ? result.size() : limit);
	}

	@GetMapping("{id}")
	public TransportationTypeData getTrainType(@PathVariable("id") String id) {
		return typeService.getById(id).map(typeDataConverter::convert).orElseThrow(() -> getNotFoundException(id));
	}
	
	@PostMapping
	public TransportationTypeData setType(@RequestBody TransportationTypeForm typeForm) {
		final TransportationType type = typeFormConverter.convertToModel(typeForm);
		return typeDataConverter.convert(typeService.save(type));
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
			return typeService.getAll();
		}
	}
	
	private ResponseStatusException getNotFoundException(String id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "Type with id " + id + " does not exist!");
	}
	
}
