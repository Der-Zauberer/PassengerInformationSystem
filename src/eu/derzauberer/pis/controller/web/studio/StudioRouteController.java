package eu.derzauberer.pis.controller.web.studio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.dto.PageDto;
import eu.derzauberer.pis.service.RouteService;

@Controller
@RequestMapping("/studio/routes")
public class StudioRouteController {
	
	@Autowired
	private RouteService routeService;
	
	@GetMapping
	public String getStationsPage(Model model,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		model.addAttribute("page", new PageDto<>(routeService, page, pageSize));
		return "/studio/routes.html";
	}

}