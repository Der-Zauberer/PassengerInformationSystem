package eu.derzauberer.pis.controller.web.studio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.entity.Operator;
import eu.derzauberer.pis.service.OperatorService;
import eu.derzauberer.pis.util.Collectable;

@Controller
@RequestMapping("/studio/operators")
public class StudioOperatorController {
	
	@Autowired
	private OperatorService operatorService;
	
	@GetMapping
	public String getOperators(Model model,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Collectable<Operator> collectable = hasSearch ? operatorService.search(search) : operatorService;
		model.addAttribute("page", collectable.getPage(page, pageSize));
		return "studio/operators.html";
	}

}
