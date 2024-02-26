package eu.derzauberer.pis.controller.studio;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.converter.FormConverter;
import eu.derzauberer.pis.dto.OperatorForm;
import eu.derzauberer.pis.dto.ResultPageDto;
import eu.derzauberer.pis.model.OperatorModel;
import eu.derzauberer.pis.persistence.Lazy;
import eu.derzauberer.pis.service.OperatorService;

@Controller
@RequestMapping("/studio/operators")
public class StudioOperatorController {
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private FormConverter<OperatorModel, OperatorForm> operatorFormConverter;
	
	@GetMapping
	public String getOperators(Model model,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Collection<Lazy<OperatorModel>> result = hasSearch ? operatorService.search(search) : operatorService.getAll();
		model.addAttribute("page", new ResultPageDto<>(page, pageSize, result));
		return "studio/operators.html";
	}
	
	@GetMapping("/edit")
	public String editOperator(@RequestParam(value = "id", required = false) String id, Model model) {
		operatorService.getById(id).map(operatorFormConverter::convertToForm).ifPresentOrElse(type -> {
			model.addAttribute("operator", type);
		}, () -> {
			model.addAttribute("operator", new OperatorForm	());
		});
		return "studio/edit/form/operator-form.html";
	}
	
	@PostMapping("/edit")
	public String editOperator(@RequestParam(value = "entity", required = false) String id, Model model, OperatorForm operatorForm) {
		final OperatorModel operator = operatorService.getById(id)
				.map(original -> operatorFormConverter.convertToModel(original, operatorForm))
				.orElseGet(() -> operatorFormConverter.convertToModel(operatorForm));
		operatorService.save(operator);
		return "redirect:/studio/operators";
	}
	
	@GetMapping("/delete")
	public String deleteOperator(@RequestParam(value = "id", required = false) String id) {
		operatorService.removeById(id);
		return "redirect:/studio/operators";
	}

}
