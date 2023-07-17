package eu.derzauberer.pis.controller.web.studio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.dto.PageDto;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.service.StationService;

@Controller
@RequestMapping("/studio/stations")
public class StudioStationController {

	@Autowired
	private StationService stationService;
	
	@GetMapping
	public String getStationsPage(Model model, 
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		model.addAttribute("stations", new PageDto<Station>(stationService, page, pageSize));
		return "/studio/stations.html";
	}
	
}