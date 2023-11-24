package eu.derzauberer.pis.controller.web.studio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.service.StationService;
import eu.derzauberer.pis.util.Result;

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
		final Result<Station> result = hasSearch ? stationService.search(search) : stationService;
		model.addAttribute("page", result.getPage(page, pageSize));
		return "studio/stations.html";
	}
	
	@GetMapping("/edit")
	public String editStation(@RequestParam(value = "id", required = false) String id, Model model) {
		stationService.getById(id).ifPresentOrElse(station -> {
			model.addAttribute("station", station);
		}, () -> {
			model.addAttribute("station", new Station("unnamed", "Unnamed"));
		});
		return "studio/edit/edit_station.html";
	}
	
}
