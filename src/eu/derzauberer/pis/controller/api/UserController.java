package eu.derzauberer.pis.controller.api;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.derzauberer.pis.dto.ResultListDto;
import eu.derzauberer.pis.dto.UserForm;
import eu.derzauberer.pis.model.UserModel;
import eu.derzauberer.pis.persistence.Lazy;
import eu.derzauberer.pis.service.UserService;
import eu.derzauberer.pis.util.NotFoundException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResultListDto<UserModel> getUsers(
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Collection<Lazy<UserModel>> result = hasSearch ? userService.search(search) : userService.getAll();
		return new ResultListDto<>(offset, limit < 1 ? result.size() : limit, result);
	}
	
	@GetMapping("{id}")
	public UserModel getUser(@PathVariable("id") String id) {
		return userService.getById(id).orElseThrow(() -> new NotFoundException("User", id));
	}
	
	@PostMapping
	public UserModel setUser(@RequestBody @Valid UserForm userForm) {
		final UserModel user = userService.getById(userForm.getId())
				.map(original -> userForm.toUserModel(original))
				.orElseGet(() -> userForm.toUserModel());
		userService.save(user);
		return user;
	}
	
	@DeleteMapping("{id}")
	public void deleteUser(@PathVariable("id") String id) {
		if (!userService.removeById(id)) throw new NotFoundException("User", id);
	}
	
	@PostMapping("/import")
	public String importUser(@RequestBody String content) {
		userService.importEntities(content);
		return "Successful imported!";
	}
	
	@GetMapping("/export")
	public Object importUser(@RequestParam(name = "download", defaultValue = "false") boolean donwload, Model model, HttpServletResponse response) throws IOException {
		if (donwload) {
			final String content = userService.exportEntities();
			response.setContentType("application/octet-stream");
			final String headerKey = "Content-Disposition";
			final String headerValue = "attachment; filename = " + userService.getName() + ".json";
			response.setHeader(headerKey, headerValue);
			final ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(content.getBytes("UTF-8"));
			outputStream.close();
			return null;
		} else {
			return userService.getAll();
		}
	}

}
