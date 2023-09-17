package eu.derzauberer.pis.controller.web.studio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.service.TypeService;

@Controller
@RequestMapping("/studio/types")
public class StudioTypeController extends StudioController {
	
	@Autowired
	private TypeService typeService;
	
	@GetMapping
	public String getStationsPage(Model model,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		getAll(typeService, model, search, page, pageSize);
		return "/studio/types.html";
	}

}
