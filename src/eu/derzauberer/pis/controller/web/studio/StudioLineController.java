package eu.derzauberer.pis.controller.web.studio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.model.Line;
import eu.derzauberer.pis.service.LineService;

@Controller
@RequestMapping("/studio/lines")
public class StudioLineController extends StudioController {
	
	@Autowired
	private LineService lineService;
	
	@GetMapping
	public String getLinesPage(Model model,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		final List<Line> lines;
		final int[] range;
		if (search == null || search.isBlank()) {
			range = calculatePageRange(page, pageSize, lineService.size());
			lines = lineService.getRange(range[0], range[1]);
		} else {
			final List<Line> searchResults = lineService.search(search);
			range = calculatePageRange(page, pageSize, searchResults.size());
			lines = searchResults.subList(range[0], range[1]);
		}
		setPageModel(lines, model, search, page, pageSize, range[2]);
		return "studio/lines.html";
	}

}
