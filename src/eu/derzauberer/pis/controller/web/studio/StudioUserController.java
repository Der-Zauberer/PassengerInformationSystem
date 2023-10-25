package eu.derzauberer.pis.controller.web.studio;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.dto.UserDto;
import eu.derzauberer.pis.dto.UserEditDto;
import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.service.UserService;
import eu.derzauberer.pis.util.Collectable;

@Controller
@RequestMapping("/studio/users")
public class StudioUserController {
	
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
		final boolean hasSearch = search != null && !search.isBlank();
		final Collectable<User> collectable = hasSearch ? userService.search(search) : userService;
		model.addAttribute("page", collectable.map(user -> modelMapper.map(user, UserDto.class)).getPage(page, pageSize));
		return "studio/users.html";
	}
	
	@GetMapping("/edit")
	public String editType(@RequestParam(value = "id", required = false) String id, Model model) {
		userService.getById(id).ifPresentOrElse(user -> {
			model.addAttribute("user", modelMapper.map(user, UserEditDto.class));
		}, () -> {
			model.addAttribute("user", new UserEditDto());
		});
		return "studio/edit/edit_user.html";
	}

}
