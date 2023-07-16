package eu.derzauberer.pis.controller.web.studio;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/studio")
public class StudioController {
	
	@GetMapping
	public String getDashboardPage() {
		return "/studio/dashboard.html";
	}

}
