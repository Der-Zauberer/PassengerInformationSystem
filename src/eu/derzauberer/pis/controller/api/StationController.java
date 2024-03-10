package eu.derzauberer.pis.controller.api;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.derzauberer.pis.dto.ResultListDto;
import eu.derzauberer.pis.dto.StationForm;
import eu.derzauberer.pis.model.StationModel;
import eu.derzauberer.pis.persistence.Lazy;
import eu.derzauberer.pis.service.StationService;
import eu.derzauberer.pis.util.NotFoundException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/stations")
public class StationController {
	
	@Autowired
	private StationService stationService;
	
	@GetMapping
	public ResultListDto<StationModel> getStations(
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Collection<Lazy<StationModel>> result = hasSearch ? stationService.search(search) : stationService.getAll();
		return new ResultListDto<>(offset, limit < 1 ? result.size() : limit, result);
	}
	
	@GetMapping("{id}")
	public StationModel getStation(@PathVariable("id") String id) {
		return stationService.getById(id).orElseThrow(() -> new NotFoundException("Station", id));
	}
	
	@PostMapping
	public StationModel setStation(@RequestBody @Valid StationForm stationForm) {
		final StationModel station = stationService.getById(stationForm.getId())
				.map(original -> stationForm.toStationModel(original))
				.orElseGet(() -> stationForm.toStationModel());
		stationService.save(station);
		return station;
	}
	
	@DeleteMapping("{id}")
	public void deleteStation(@PathVariable("id") String id) {
		if (!stationService.removeById(id)) throw new NotFoundException("Station", id);
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
	
}
