package eu.derzauberer.pis.controller.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.derzauberer.pis.converter.DataConverter;
import eu.derzauberer.pis.converter.FormConverter;
import eu.derzauberer.pis.service.StationService;
import eu.derzauberer.pis.structure.data.StationData;
import eu.derzauberer.pis.structure.form.StationForm;
import eu.derzauberer.pis.structure.model.Station;
import eu.derzauberer.pis.util.Result;
import eu.derzauberer.pis.util.ResultListDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/stations")
public class StationController {
	
	@Autowired
	private StationService stationService;
	
	@Autowired
	private DataConverter<Station, StationData> stationDataConverter;
	
	@Autowired
	private FormConverter<Station, StationForm> stationFormConverter;
	
	@GetMapping
	public ResultListDto<StationData> getStations(
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Result<Station> result = hasSearch ? stationService.search(search) : stationService;
		return result.map(stationDataConverter::convert).getList(offset, limit == -1 ? result.size() : limit);
	}
	
	@GetMapping("{id}")
	public StationData getStation(@PathVariable("id") String id) {
		return stationService.getById(id).map(stationDataConverter::convert).orElseThrow(() -> getNotFoundException(id));
	}
	
	@PostMapping
	public StationData setStation(@RequestBody StationForm stationForm) {
		final Station station = stationFormConverter.convertToModel(stationForm);
		return stationDataConverter.convert(stationService.save(station));
	}
	
	@DeleteMapping("{id}")
	public void deleteStation(@PathVariable("id") String id) {
		if (!stationService.removeById(id)) throw getNotFoundException(id);
	}
	
	@PostMapping("/import")
	public String importStations(@RequestBody String content) {
		stationService.importEntities(content);
		return "Successful imported!";
	}
	
	@GetMapping("/export")
	public Object importStations(@RequestParam(name = "download", defaultValue = "false") boolean donwload, Model model, HttpServletResponse response) throws IOException {
		if (donwload) {
			final String content = stationService.exportEntities();
			response.setContentType("application/octet-stream");
			final String headerKey = "Content-Disposition";
			final String headerValue = "attachment; filename = " + stationService.getName() + ".json";
			response.setHeader(headerKey, headerValue);
			final ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(content.getBytes("UTF-8"));
			outputStream.close();
			return null;
		} else {
			return stationService.getAll();
		}
	}

	private ResponseStatusException getNotFoundException(String id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "Station with id " + id + " does not exist!");
	}
	
}
