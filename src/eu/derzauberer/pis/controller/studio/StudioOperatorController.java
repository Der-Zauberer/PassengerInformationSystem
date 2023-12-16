package eu.derzauberer.pis.controller.studio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.converter.DataConverter;
import eu.derzauberer.pis.converter.FormConverter;
import eu.derzauberer.pis.service.OperatorService;
import eu.derzauberer.pis.structure.data.OperatorData;
import eu.derzauberer.pis.structure.form.OperatorForm;
import eu.derzauberer.pis.structure.model.Operator;
import eu.derzauberer.pis.util.Result;

@Controller
@RequestMapping("/studio/operators")
public class StudioOperatorController {
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private DataConverter<Operator, OperatorData> operatorDataConverter;
	
	@Autowired
	private FormConverter<Operator, OperatorForm> operatorFormConverter;
	
	@GetMapping
	public String getOperators(Model model,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Result<Operator> result = hasSearch ? operatorService.search(search) : operatorService;
		model.addAttribute("page", result.map(operatorDataConverter::convert).getPage(page, pageSize));
		return "studio/operators.html";
	}
	
	@GetMapping("/edit")
	public String editType(@RequestParam(value = "id", required = false) String id, Model model) {
		operatorService.getById(id).map(operatorFormConverter::convertToForm).ifPresentOrElse(type -> {
			model.addAttribute("operator", type);
		}, () -> {
			model.addAttribute("operator", new OperatorForm	());
		});
		return "studio/edit/edit_operator.html";
	}

}
