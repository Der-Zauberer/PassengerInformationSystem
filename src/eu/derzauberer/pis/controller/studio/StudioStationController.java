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
import eu.derzauberer.pis.service.StationService;
import eu.derzauberer.pis.structure.data.StationData;
import eu.derzauberer.pis.structure.form.StationForm;
import eu.derzauberer.pis.structure.model.Station;
import eu.derzauberer.pis.util.Result;

@Controller
@RequestMapping("/studio/stations")
public class StudioStationController {

	@Autowired
	private StationService stationService;
	
	@Autowired
	private DataConverter<Station, StationData> stationDataConverter;
	
	@Autowired
	private FormConverter<Station, StationForm> stationFormConverter;
	
	@GetMapping
	public String getStations(Model model,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Result<Station> result = hasSearch ? stationService.search(search) : stationService;
		model.addAttribute("page", result.map(stationDataConverter::convert).getPage(page, pageSize));
		return "studio/stations.html";
	}
	
	@GetMapping("/edit")
	public String editStation(@RequestParam(value = "id", required = false) String id, Model model) {
		stationService.getById(id).map(stationFormConverter::convertToForm).ifPresentOrElse(station -> {
			model.addAttribute("station", station);
		}, () -> {
			model.addAttribute("station", new StationForm());
		});
		return "studio/edit/form/station-form.html";
	}
	
	@PostMapping("/edit")
	public String editStation(@RequestParam(value = "entity", required = false) String id, Model model, StationForm stationForm) {
		final Station station = stationService.getById(id)
				.map(original -> stationFormConverter.convertToModel(original, stationForm))
				.orElseGet(() -> stationFormConverter.convertToModel(stationForm));
		stationService.save(station);
		return "redirect:/studio/stations";
	}
	
	@GetMapping("/delete")
	public String deleteStation(@RequestParam(value = "id", required = false) String id) {
		stationService.removeById(id);
		return "redirect:/studio/stations";
	}
	
}
