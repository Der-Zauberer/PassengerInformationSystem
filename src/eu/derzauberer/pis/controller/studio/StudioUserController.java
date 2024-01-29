package eu.derzauberer.pis.controller.studio;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.converter.DataConverter;
import eu.derzauberer.pis.converter.FormConverter;
import eu.derzauberer.pis.persistence.Lazy;
import eu.derzauberer.pis.service.UserService;
import eu.derzauberer.pis.structure.dto.ResultPageDto;
import eu.derzauberer.pis.structure.dto.UserData;
import eu.derzauberer.pis.structure.dto.UserForm;
import eu.derzauberer.pis.structure.enums.UserRole;
import eu.derzauberer.pis.structure.model.UserModel;

@Controller
@RequestMapping("/studio/users")
public class StudioUserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private DataConverter<UserModel, UserData> userDataConverter;
	
	@Autowired
	private FormConverter<UserModel, UserForm> userFormConverter;
	
	
	@GetMapping
	public String getUsers(Model model, 
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Collection<Lazy<UserModel>> result = hasSearch ? userService.search(search) : userService.getAll();
		model.addAttribute("page", new ResultPageDto<>(page, pageSize, result.stream().map(lazy -> lazy.map(userDataConverter::convert)).toList()));
		return "studio/users.html";
	}
	
	@GetMapping("/edit")
	public String editUser(@RequestParam(value = "id", required = false) String id, Model model) {
		final UserForm user = userService.getById(id).map(userFormConverter::convertToForm).orElseGet(() -> new UserForm());
		model.addAttribute("user", user);
		model.addAttribute("roles", UserRole.values());
		return "studio/edit/form/user-form.html";
	}
	
	@PostMapping("/edit")
	public String editUser(@RequestParam(value = "entity", required = false) String id, Model model, UserForm userForm) {
		final UserModel user = userService.getById(id)
				.map(original -> userFormConverter.convertToModel(original, userForm))
				.orElseGet(() -> userFormConverter.convertToModel(userForm));
		userService.save(user);
		return "redirect:/studio/users";
	}
	
	@GetMapping("/delete")
	public String deleteUser(@RequestParam(value = "id", required = false) String id) {
		userService.removeById(id);
		return "redirect:/studio/users";
	}
	
}
