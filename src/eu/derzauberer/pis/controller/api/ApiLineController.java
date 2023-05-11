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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.derzauberer.pis.dto.ListDto;
import eu.derzauberer.pis.model.LineLiveData;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.model.StationTrafficEntry;
import eu.derzauberer.pis.service.LineService;
import eu.derzauberer.pis.service.StationService;

@RestController
@RequestMapping("/api/lines")
public class ApiLineController {
	
	@Autowired
	private LineService lineService;
	
	@Autowired
	private StationService stationService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping("/arrivals/{stationId}/{date}")
	public ListDto<StationTrafficEntry> getArrivals(
			@PathVariable("stationId") String stationId,
			@PathVariable("date") String date,
			@RequestParam(name = "hour", required = false, defaultValue = "0") int hour,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset
			) {
		final Station station = stationService.getById(stationId).orElseThrow(() -> getNotFoundException("Station", stationId));
		final List<StationTrafficEntry> entries = lineService.findArrivalsSinceHour(station, null, limit).stream().toList();
		return new ListDto<>(entries, limit == -1 ? entries.size() : limit, offset);
	}
	
	@GetMapping("/departures/{stationId}/{date}")
	public ListDto<StationTrafficEntry> getDepartures(
			@PathVariable("stationId") String stationId,
			@PathVariable("date") String date,
			@RequestParam(name = "hour", required = false, defaultValue = "0") int hour,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset
			) {
		final Station station = stationService.getById(stationId).orElseThrow(() -> getNotFoundException("Station", stationId));
		final List<StationTrafficEntry> entries = lineService.findDeparturesSinceHour(station, null, limit).stream().toList();
		return new ListDto<>(entries, limit == -1 ? entries.size() : limit, offset);
	}
	
	@GetMapping("{id}")
	public LineLiveData getLine(@PathVariable("id") String id) {
		return lineService.getById(id).orElseThrow(() -> getNotFoundException("Line", id));
	}
	
	@PostMapping
	public LineLiveData setLine(LineLiveData line) {
		lineService.add(line);
		return line;
	}
	
	@PutMapping
	public LineLiveData updateLine(LineLiveData line) {
		final LineLiveData existingLine = lineService.getById(line.getId()).orElseThrow(() -> getNotFoundException("Line", line.getId()));
		modelMapper.map(line, existingLine);
		return existingLine;
	}
	
	@DeleteMapping("{id}")
	public void deleteLine(@PathVariable("id") String id) {
		if (!lineService.removeById(id)) throw getNotFoundException("Line", id);
	}
	
	private ResponseStatusException getNotFoundException(String entity, String id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " with id " + id + " does not exist!");
	}

}
