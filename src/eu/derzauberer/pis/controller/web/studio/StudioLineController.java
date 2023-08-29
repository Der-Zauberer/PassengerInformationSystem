package eu.derzauberer.pis.controller.web.studio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.dto.PageDto;
import eu.derzauberer.pis.service.LineService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/studio/lines")
public class StudioLineController {
	
	@Autowired
	private LineService lineService;
	
	@GetMapping
	public String getStationsPage(Model model,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		model.addAttribute("page", new PageDto<>(lineService, page, pageSize));
		return "/studio/lines.html";
	}
	
	@GetMapping("/export")
	public void exportStations(Model model, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
		final String content = lineService.exportEntities();
		response.setContentType("application/octet-stream");
		final String headerKey = "Content-Disposition";
		final String headerValue = "attachment; filename = " + lineService.getName() + ".json";
		response.setHeader(headerKey, headerValue);
		final ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(content.getBytes("UTF-8"));
		outputStream.close();
	}

}
