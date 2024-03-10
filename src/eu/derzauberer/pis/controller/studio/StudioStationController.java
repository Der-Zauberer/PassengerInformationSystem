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
import eu.derzauberer.pis.dto.StationForm;
import eu.derzauberer.pis.model.StationModel;
import eu.derzauberer.pis.persistence.Lazy;
import eu.derzauberer.pis.service.StationService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/studio/stations")
public class StudioStationController {

	@Autowired
	private StationService stationService;
	
	@GetMapping
	public String getStations(Model model,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Collection<Lazy<StationModel>> result = hasSearch ? stationService.search(search) : stationService.getAll();
		model.addAttribute("page", new ResultPageDto<>(page, pageSize, result));
		return "studio/stations.html";
	}
	
	@GetMapping("/edit")
	public String editStation(@RequestParam(value = "id", required = false) String id, Model model) {
		stationService.getById(id).map(StationForm::new).ifPresentOrElse(station -> {
			model.addAttribute("station", station);
		}, () -> {
			model.addAttribute("station", new StationForm());
		});
		return "studio/edit/form/station-form.html";
	}
	
	@PostMapping("/edit")
	public String editStation(@RequestParam(value = "entity", required = false) String id, Model model, @Valid StationForm stationForm) {
		final StationModel station = stationService.getById(id)
				.map(original -> stationForm.toStationModel(original))
				.orElseGet(() -> stationForm.toStationModel());
		stationService.save(station);
		return "redirect:/studio/stations";
	}
	
	@GetMapping("/delete")
	public String deleteStation(@RequestParam(value = "id", required = false) String id) {
		stationService.removeById(id);
		return "redirect:/studio/stations";
	}
	
}
