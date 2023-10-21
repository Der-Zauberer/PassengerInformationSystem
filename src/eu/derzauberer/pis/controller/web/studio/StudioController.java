package eu.derzauberer.pis.controller.web.studio;

import java.util.List;

import org.springframework.ui.Model;

import eu.derzauberer.pis.dto.PageDto;
import eu.derzauberer.pis.service.EntityService;

public abstract class StudioController {
	
	public int[] calculatePageRange(int page, int pageSize, int totalSize) {
		final int pageAmount = (int) Math.ceil(totalSize / pageSize) + 1;
		if (page > pageAmount) throw new IllegalArgumentException("The page is larger than the total amount of pages!");
		final int[] range = new int[3];
		range[0] = (page - 1) * pageSize;
		range[1] = (page == pageAmount) ? totalSize : range[0] + pageSize;
		range[2] = pageAmount;
		return range;
	}
	
	public <T> void setPageModel(List<T> results, Model model, String search, int page, int pageSize, int pageAmount) {
		final PageDto<T> pageDto = new PageDto<>();
		pageDto.setPage(page);
		pageDto.setPageSize(pageSize);
		pageDto.setPageAmount(pageAmount);
		pageDto.setResults(results);
		model.addAttribute("page", pageDto);
	}
	
	@SuppressWarnings("unchecked")
	public <T> void setPageModel(EntityService<?> service, Model model, String search, int page, int pageSize) {
		List<T> entities;
		final int[] range;
		if (search == null || search.isBlank()) {
			range = calculatePageRange(page, pageSize, service.size());
			entities = (List<T>) service.getRange(range[0], range[1]);
		} else {
			final List<T> searchResults = (List<T>) service.search(search);
			range = calculatePageRange(page, pageSize, searchResults.size());
			entities = searchResults.subList(range[0], range[1]);
		}
		setPageModel(entities, model, search, page, pageSize, range[2]);
	}
	
}
