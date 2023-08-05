package eu.derzauberer.pis.controller.api;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.derzauberer.pis.dto.ListDto;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.service.StationService;

@RestController
@RequestMapping("/api/stations")
public class StationController {
	
	@Autowired
	private StationService stationService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public ListDto<Station> getStations(
			@RequestParam(name = "query", required = false) String query,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		if (query != null) {
			final List<Station> stations = stationService.searchByName(query);
			return new ListDto<>(stations, offset, limit == -1 ? stations.size() : limit);
		}
		return new ListDto<>(stationService, offset, limit == -1 ? stationService.size() : limit);
	}
	
	@GetMapping("{id}")
	public Station getStation(@PathVariable("id") String id) {
		return stationService.getById(id).orElseThrow(() -> getNotFoundException(id));
	}
	
	@PostMapping
	public Station setStation(@RequestBody Station station) {
		stationService.add(station);
		return station;
	}
	
	@PutMapping
	public Station updateStation(@RequestBody Station station) {
		final Station existingStation = stationService.getById(station.getId()).orElseThrow(() -> getNotFoundException(station.getId()));
		modelMapper.map(station, existingStation);
		stationService.add(existingStation);
		return existingStation;
	}
	
	@DeleteMapping("{id}")
	public void deleteStation(@PathVariable("id") String id) {
		if (!stationService.removeById(id)) throw getNotFoundException(id);
	}
	
	@PostMapping("/import")
	public String importStations(@RequestBody String content) {
		stationService.extractEntities(content);
		return "Successful imported!";
	}

	private ResponseStatusException getNotFoundException(String id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "Station with id " + id + " does not exist!");
	}
	
}
