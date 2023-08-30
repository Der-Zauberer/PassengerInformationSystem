package eu.derzauberer.pis.controller.api;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
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
import eu.derzauberer.pis.model.Line;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.model.StationTrafficEntry;
import eu.derzauberer.pis.service.LineService;
import eu.derzauberer.pis.service.StationService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

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
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit
			) {
		return getTraffic(false, stationId, date, hour, offset, limit);
	}
	
	@GetMapping("/departures/{stationId}/{date}")
	public ListDto<StationTrafficEntry> getDepartures(
			@PathVariable("stationId") String stationId,
			@PathVariable("date") String date,
			@RequestParam(name = "hour", required = false, defaultValue = "0") int hour,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit
			) {
		return getTraffic(true, stationId, date, hour, offset, limit);
	}
	
	private ListDto<StationTrafficEntry> getTraffic(boolean arrival, String stationId, String date, int hour, int offset, int limit) {
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
	public Line setLine(@RequestBody Line line) {
		lineService.add(line);
		return line;
	}
	
	@PutMapping
	public Line updateLine(@RequestBody Line line) {
		final Line existingLine = lineService.getById(line.getId()).orElseThrow(() -> getNotFoundException("Line", line.getId()));
		modelMapper.map(line, existingLine);
		lineService.add(existingLine);
		return existingLine;
	}
	
	@DeleteMapping("{id}")
	public void deleteLine(@PathVariable("id") String id) {
		if (!lineService.removeById(id)) throw getNotFoundException("Line", id);
	}
	
	@PostMapping("/import")
	public String importStations(@RequestBody String content) {
		lineService.importEntities(content);
		return "Successful imported!";
	}
	
	@GetMapping("/export")
	public Object importStations(@RequestParam(name = "download", defaultValue = "false") boolean donwload, Model model, HttpServletResponse response) throws IOException {
		if (donwload) {
			final String content = lineService.exportEntities();
			response.setContentType("application/octet-stream");
			final String headerKey = "Content-Disposition";
			final String headerValue = "attachment; filename = " + lineService.getName() + ".json";
			response.setHeader(headerKey, headerValue);
			final ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(content.getBytes("UTF-8"));
			outputStream.close();
			return null;
		} else {
			return lineService.getList();
		}
	}
	
	private ResponseStatusException getNotFoundException(String entity, String id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " with id " + id + " does not exist!");
	}

}
