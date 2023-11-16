package eu.derzauberer.pis.controller.web.studio;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.dto.UserDto;
import eu.derzauberer.pis.dto.UserEditDto;
import eu.derzauberer.pis.enums.UserRole;
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
	
	@Autowired
	private TypeMap<User, UserEditDto> userEditDtoModelMapper;

	@Autowired
	private TypeMap<UserEditDto, User> userModelMapper;
	
	
	@GetMapping
	public String getUsers(Model model, 
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
	public String editUser(@RequestParam(value = "id", required = false) String id, Model model) {
		final UserEditDto user = userService.getById(id)
				.map(userEditDtoModelMapper::map)
				.orElseGet(() -> new UserEditDto());
		model.addAttribute("user", user);
		model.addAttribute("roles", UserRole.values());
		model.addAttribute("defaultRole", UserRole.USER);
		return "studio/edit/edit_user.html";
	}
	
	@PostMapping("/edit")
	public String editUser(@RequestParam(value = "entity", required = false) String id, Model model, UserEditDto userDto) {
		final User user = userService.getById(id).orElseGet(() -> new User(userDto.getId(), userDto.getName()));
		userModelMapper.map(userDto, user);
		if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
			user.setPassword(userService.hashPassword(userDto.getPassword()));
		}
		userService.save(user);
		return "redirect:/studio/users";
	}
	
	@GetMapping("/delete")
	public String deleteUser(@RequestParam(value = "id", required = false) String id) {
		userService.removeById(id);
		return "redirect:/studio/users";
	}
	
}
