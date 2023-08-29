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
import eu.derzauberer.pis.service.UserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/studio/users")
public class StudioUserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public String getUsersPage(Model model, 
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		if (search != null && !search.isEmpty()) {
			final String searchString = search.replace('+', ' ');
			model.addAttribute("page", new PageDto<>(userService.searchByName(searchString), page, pageSize));
		} else {			
			model.addAttribute("page", new PageDto<>(userService, page, pageSize));
		}
		return "/studio/users.html";
	}
	
	@GetMapping("/export")
	public void exportStations(Model model, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
		final String content = userService.exportEntities();
		response.setContentType("application/octet-stream");
		final String headerKey = "Content-Disposition";
		final String headerValue = "attachment; filename = " + userService.getName() + ".json";
		response.setHeader(headerKey, headerValue);
		final ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(content.getBytes("UTF-8"));
		outputStream.close();
	}

}