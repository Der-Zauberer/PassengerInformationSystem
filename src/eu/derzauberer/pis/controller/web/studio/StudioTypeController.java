package eu.derzauberer.pis.controller.web.studio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.model.Type;
import eu.derzauberer.pis.model.Type.Classifican;
import eu.derzauberer.pis.service.TypeService;

@Controller
@RequestMapping("/studio/types")
public class StudioTypeController extends StudioController {
	
	@Autowired
	private TypeService typeService;
	
	@GetMapping
	public String getTypesPage(Model model,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		getAll(typeService, model, search, page, pageSize);
		return "/studio/types.html";
	}
	
	@GetMapping("/edit")
	public String editType(@RequestParam(value = "id", required = false) String id, Model model) {
		typeService.getById(id).ifPresentOrElse(type -> {
			model.addAttribute("type", type);
		}, () -> {
			model.addAttribute("type", new Type("unnamed", "UNNAMED", Classifican.PASSENGER_REGIONAL));
		});
		return "/studio/edit/types.html";
	}

}
