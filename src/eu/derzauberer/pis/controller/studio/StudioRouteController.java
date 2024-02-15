package eu.derzauberer.pis.controller.studio;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.dto.ResultPageDto;
import eu.derzauberer.pis.model.RouteModel;
import eu.derzauberer.pis.persistence.Lazy;
import eu.derzauberer.pis.service.RouteService;

@Controller
@RequestMapping("/studio/routes")
public class StudioRouteController {
	
	@Autowired
	private RouteService routeService;
	
	@GetMapping
	public String getRoutes(Model model,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Collection<Lazy<RouteModel>> result = hasSearch ? routeService.search(search) : routeService.getAll();
		model.addAttribute("page", new ResultPageDto<>(page, pageSize, result));
		return "studio/routes.html";
	}

}
