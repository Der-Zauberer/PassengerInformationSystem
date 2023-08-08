package eu.derzauberer.pis.controller.web.studio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.dto.PageDto;
import eu.derzauberer.pis.service.UserService;

@Controller
@RequestMapping("/studio/users")
public class StudioUserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public String getUsersPage(Model model, 
			@RequestParam(name = "query", required = false) String query,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		if (query != null && !query.isEmpty()) {
			final String serach = query.replace('+', ' ');
			model.addAttribute("page", new PageDto<>(userService.searchByName(serach), page, pageSize));
		} else {			
			model.addAttribute("page", new PageDto<>(userService, page, pageSize));
		}
		return "/studio/users.html";
	}

}
