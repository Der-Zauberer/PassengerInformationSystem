package eu.derzauberer.pis.controller.web.studio;

import org.springframework.ui.Model;

import eu.derzauberer.pis.dto.PageDto;
import eu.derzauberer.pis.service.EntityService;

public abstract class StudioController {
	
	public void getAll(EntityService<?> service, Model model, String search, int page, int pageSize) {
		if (search != null && !search.isEmpty()) {
			model.addAttribute("page", new PageDto<>(service.search(search), page, pageSize));
		} else {			
			model.addAttribute("page", new PageDto<>(service, page, pageSize));
		}
	}

}
