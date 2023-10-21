package eu.derzauberer.pis.controller.web.studio;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.dto.UserDto;
import eu.derzauberer.pis.service.UserService;

@Controller
@RequestMapping("/studio/users")
public class StudioUserController extends StudioController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public String getUsersPage(Model model, 
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		List<UserDto> users;
		final int[] range;
		if (search == null || search.isBlank()) {
			range = calculatePageRange(page, pageSize, userService.size());
			users = userService.getRange(range[0], range[1]).stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
		} else {
			final List<UserDto> searchResults = userService.search(search).stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
			range = calculatePageRange(page, pageSize, searchResults.size());
			users = searchResults.subList(range[0], range[1]);
		}
		setPageModel(users, model, search, page, pageSize, range[2]);
		return "studio/users.html";
	}

}
