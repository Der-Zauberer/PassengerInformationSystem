package eu.derzauberer.pis.controller.studio;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.dto.ResultPageDto;
import eu.derzauberer.pis.dto.TransportationTypeForm;
import eu.derzauberer.pis.enums.TransportationClassification;
import eu.derzauberer.pis.enums.TransportationVehicle;
import eu.derzauberer.pis.model.TransportationTypeModel;
import eu.derzauberer.pis.persistence.Lazy;
import eu.derzauberer.pis.service.TypeService;
import jakarta.validation.Valid;

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
		final Collection<Lazy<TransportationTypeModel>> result = hasSearch ? typeService.search(search) : typeService.getAll();
		model.addAttribute("page", new ResultPageDto<>(page, pageSize, result));
		return "studio/types.html";
	}
	
	@GetMapping("/edit")
	public String editType(@RequestParam(value = "id", required = false) String id, Model model) {
		typeService.getById(id).map(TransportationTypeForm::new).ifPresentOrElse(type -> {
			model.addAttribute("type", type);
		}, () -> {
			model.addAttribute("type", new TransportationTypeForm());
		});
		model.addAttribute("vehicles", TransportationVehicle.values());
		model.addAttribute("classifications", TransportationClassification.values());
		return "studio/edit/form/type-form.html";
	}
	
	@PostMapping("/edit")
	public String editUser(@RequestParam(value = "entity", required = false) String id, Model model, @Valid TransportationTypeForm typeForm) {
		final TransportationTypeModel type = typeService.getById(id)
				.map(original -> typeForm.toTransportationTypeModel(original))
				.orElseGet(() -> typeForm.toTransportationTypeModel());
		typeService.save(type);
		return "redirect:/studio/types";
	}
	
	@GetMapping("/delete")
	public String deleteUser(@RequestParam(value = "id", required = false) String id) {
		typeService.removeById(id);
		return "redirect:/studio/types";
	}

}
