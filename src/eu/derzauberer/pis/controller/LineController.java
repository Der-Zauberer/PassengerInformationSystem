package eu.derzauberer.pis.controller;

import java.time.LocalDateTime;
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
import eu.derzauberer.pis.model.Line;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.model.StationTrafficEntry;
import eu.derzauberer.pis.service.LineService;
import eu.derzauberer.pis.service.StationService;

@RestController
@RequestMapping("/api/lines")
public class LineController {
	
	@Autowired
	private LineService lineService;
	
	@Autowired
	private StationService stationService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping("/{arrivalOrDeparture}/{stationId}/{date}")
	public ListDto<StationTrafficEntry> getArrivals(
			@PathVariable("stationId") String stationId,
			@PathVariable("date") String date,
			@RequestParam(name = "hour", required = false, defaultValue = "0") int hour,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset
			) {
		return getTraffic(false, stationId, date, hour, limit, offset);
	}
	
	@GetMapping("/departures/{stationId}/{date}")
	public ListDto<StationTrafficEntry> getDepartures(
			@PathVariable("stationId") String stationId,
			@PathVariable("date") String date,
			@RequestParam(name = "hour", required = false, defaultValue = "0") int hour,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset
			) {
		return getTraffic(true, stationId, date, hour, limit, offset);
	}
	
	private ListDto<StationTrafficEntry> getTraffic(boolean arrival, String stationId, String date, int hour, int limit, int offset) {
		final Station station = stationService.getById(stationId).orElseThrow(() -> getNotFoundException("Station", stationId));
		if (!date.matches("^\\d{8}$")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date format " + date + " is invalid, it has to be YYYYMMDD");
		final LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)), Integer.parseInt(date.substring(6, 8)), hour, 0);
		final List<StationTrafficEntry> entries = (!arrival ? lineService.findArrivalsSinceHour(station, dateTime, limit) : lineService.findDeparturesSinceHour(station, dateTime, limit)).stream().toList();
		final int total = !arrival ? lineService.getAmountOfArrivalsSinceHour(station, dateTime, limit) : lineService.getAmountOfDeparturesSinceHour(station, dateTime, limit);
		return new ListDto<>(entries, limit == -1 ? entries.size() : limit, offset).manipulteTotal(total);
	}
	
	@GetMapping("{id}")
	public Line getLine(@PathVariable("id") String id) {
		return lineService.getById(id).orElseThrow(() -> getNotFoundException("Line", id));
	}
	
	@PostMapping
	public Line setLine(Line line) {
		lineService.add(line);
		return line;
	}
	
	@PutMapping
	public Line updateLine(Line line) {
		final Line existingLine = lineService.getById(line.getId()).orElseThrow(() -> getNotFoundException("Line", line.getId()));
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
