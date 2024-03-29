package eu.derzauberer.pis.controller.api;

import java.io.IOException;
import java.util.Collection;

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

import eu.derzauberer.pis.dto.OperatorForm;
import eu.derzauberer.pis.dto.ResultListDto;
import eu.derzauberer.pis.model.OperatorModel;
import eu.derzauberer.pis.persistence.Lazy;
import eu.derzauberer.pis.service.OperatorService;
import eu.derzauberer.pis.util.NotFoundException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/operators")
public class OperatorController {
	
	@Autowired
	private OperatorService operatorService;
	
	@GetMapping
	public ResultListDto<OperatorModel> getOperators(
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Collection<Lazy<OperatorModel>> result = hasSearch ? operatorService.search(search) : operatorService.getAll();
		return new ResultListDto<>(offset, limit < 1 ? result.size() : limit, result);
	}
	
	@GetMapping("{id}")
	public OperatorModel getOperator(@PathVariable("id") String id) {
		return operatorService.getById(id).orElseThrow(() -> new NotFoundException("Operator", id));
	}
	
	@PostMapping
	public OperatorModel setOperator(@RequestBody @Valid OperatorForm operatorForm) {
		final OperatorModel operator = operatorService.getById(operatorForm.getId())
				.map(original -> operatorForm.toOperatorModel(original))
				.orElseGet(() -> operatorForm.toOperatorModel());
		operatorService.save(operator);
		return operator;
	}
	
	@DeleteMapping("{id}")
	public void deleteOperator(@PathVariable("id") String id) {
		if (!operatorService.removeById(id)) throw new NotFoundException("Operator", id);
	}
	
	@PostMapping("/import")
	public String importOperators(@RequestBody String content) {
		operatorService.importEntities(content);
		return "Successful imported!";
	}
	
	@GetMapping("/export")
	public Object importOperators(@RequestParam(name = "download", defaultValue = "false") boolean donwload, Model model, HttpServletResponse response) throws IOException {
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

}
