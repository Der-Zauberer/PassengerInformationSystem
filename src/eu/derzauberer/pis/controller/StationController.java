package eu.derzauberer.pis.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset
			) {
		final List<Station> stations = query != null ? stationService.searchByName(query) : stationService.getList();
		return new ListDto<>(stations, limit == -1 ? stations.size() : limit, offset);
	}
	
	@GetMapping("{id}")
	public Station getStation(@PathVariable("id") String id) {
		return stationService.getById(id).orElseThrow(() -> getNotFoundException(id));
	}
	
	@PostMapping
	public Station setStation(Station station) {
		stationService.add(station);
		return station;
	}
	
	@PutMapping
	public Station updateStation(Station station) {
		final Station existingStation = stationService.getById(station.getId()).orElseThrow(() -> getNotFoundException(station.getId()));
		modelMapper.map(station, existingStation);
		return existingStation;
	}
	
	@DeleteMapping("{id}")
	public void deleteStation(@PathVariable("id") String id) {
		if (!stationService.removeById(id)) throw getNotFoundException(id);
	}

	private ResponseStatusException getNotFoundException(String id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "Station with id " + id + " does not exist!");
	}
	
}
