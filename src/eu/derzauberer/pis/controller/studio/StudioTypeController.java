package eu.derzauberer.pis.controller.studio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.converter.DataConverter;
import eu.derzauberer.pis.converter.FormConverter;
import eu.derzauberer.pis.service.TypeService;
import eu.derzauberer.pis.structure.dto.TransportationTypeData;
import eu.derzauberer.pis.structure.dto.TransportationTypeForm;
import eu.derzauberer.pis.structure.enums.TransportationClassification;
import eu.derzauberer.pis.structure.enums.TransportationVehicle;
import eu.derzauberer.pis.structure.model.TransportationTypeModel;
import eu.derzauberer.pis.util.Result;

@Controller
@RequestMapping("/studio/types")
public class StudioTypeController {
	
	@Autowired
	private TypeService typeService;
	
	@Autowired
	private DataConverter<TransportationTypeModel, TransportationTypeData> typeDataConverter;
	
	@Autowired
	private FormConverter<TransportationTypeModel, TransportationTypeForm> typeFormConverter;
	
	@GetMapping
	public String getTypes(Model model,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Result<TransportationTypeModel> result = hasSearch ? typeService.search(search) : typeService;
		model.addAttribute("page", result.map(typeDataConverter::convert).getPage(page, pageSize));
		return "studio/types.html";
	}
	
	@GetMapping("/edit")
	public String editType(@RequestParam(value = "id", required = false) String id, Model model) {
		typeService.getById(id).map(typeFormConverter::convertToForm).ifPresentOrElse(type -> {
			model.addAttribute("type", type);
		}, () -> {
			model.addAttribute("type", new TransportationTypeForm());
		});
		model.addAttribute("vehicles", TransportationVehicle.values());
		model.addAttribute("classifications", TransportationClassification.values());
		return "studio/edit/form/type-form.html";
	}
	
	@PostMapping("/edit")
	public String editUser(@RequestParam(value = "entity", required = false) String id, Model model, TransportationTypeForm typeForm) {
		final TransportationTypeModel type = typeService.getById(id)
				.map(original -> typeFormConverter.convertToModel(original, typeForm))
				.orElseGet(() -> typeFormConverter.convertToModel(typeForm));
		typeService.save(type);
		return "redirect:/studio/types";
	}
	
	@GetMapping("/delete")
	public String deleteUser(@RequestParam(value = "id", required = false) String id) {
		typeService.removeById(id);
		return "redirect:/studio/types";
	}

}
