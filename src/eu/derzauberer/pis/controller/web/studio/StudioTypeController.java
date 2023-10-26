package eu.derzauberer.pis.controller.web.studio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.enums.TransportationClassification;
import eu.derzauberer.pis.enums.TransportationVehicle;
import eu.derzauberer.pis.model.TransportationType;
import eu.derzauberer.pis.service.TypeService;
import eu.derzauberer.pis.util.Collectable;

@Controller
@RequestMapping("/studio/types")
public class StudioTypeController {
	
	@Autowired
	private TypeService typeService;
	
	@GetMapping
	public String getTypes(Model model,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Collectable<TransportationType> collectable = hasSearch ? typeService.search(search) : typeService;
		model.addAttribute("page", collectable.getPage(page, pageSize));
		return "studio/types.html";
	}
	
	@GetMapping("/edit")
	public String editType(@RequestParam(value = "id", required = false) String id, Model model) {
		typeService.getById(id).ifPresentOrElse(type -> {
			model.addAttribute("type", type);
		}, () -> {
			model.addAttribute("type", new TransportationType(null, null, TransportationVehicle.TRAIN, TransportationClassification.REGIONAL));
		});
		model.addAttribute("transportationVehicles", TransportationVehicle.values());
		model.addAttribute("transportationClassifications", TransportationClassification.values());
		return "studio/edit/edit_type.html";
	}

}
