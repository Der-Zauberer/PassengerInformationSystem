package eu.derzauberer.pis.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.derzauberer.pis.dto.ListDto;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.service.StationService;

@RestController
@RequestMapping("/api/stations")
public class ApiStationController {
	
	@Autowired
	private StationService stationService;
	
	@GetMapping
	public ListDto<Station> getStations(
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		return new ListDto<>(offset, limit , stationService.getList());
	}
	
	@GetMapping("{id}")
	public Station getStation(@PathVariable("id") String id) {
		return stationService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

}
