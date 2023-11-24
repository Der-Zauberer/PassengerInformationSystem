package eu.derzauberer.pis.controller.api;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.derzauberer.pis.data.UserData;
import eu.derzauberer.pis.data.UserInfoData;
import eu.derzauberer.pis.form.UserForm;
import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.service.UserService;
import eu.derzauberer.pis.util.Result;
import eu.derzauberer.pis.util.ResultListDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public ResultListDto<UserInfoData> getUsers(
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Result<User> result = hasSearch ? userService.search(search) : userService;
		return result.map((user) -> modelMapper.map(user, UserInfoData.class)).getList(offset, limit == -1 ? result.size() : limit);
	}
	
	@GetMapping("{id}")
	public UserInfoData getUser(@PathVariable("id") String id) {
		return userService.getById(id)
				.map((user) -> modelMapper.map(user, UserInfoData.class))
				.orElseThrow(() -> getNotFoundException(id));
	}
	
	@PostMapping
	public UserData setUser(@RequestBody UserForm user) {
		final User mappedUser = new User(user.getId(), user.getName());
		modelMapper.map(user, mappedUser);
		userService.save(mappedUser);
		return modelMapper.map(mappedUser, UserData.class);
	}
	
	@PutMapping
	public UserData updateUser(@RequestBody UserForm user) {
		final User existingUser = userService.getById(user.getId()).orElseThrow(() -> getNotFoundException(user.getId()));
		modelMapper.map(user, existingUser);
		userService.save(existingUser);
		return modelMapper.map(existingUser, UserData.class);
	}
	
	@DeleteMapping("{id}")
	public void deleteUser(@PathVariable("id") String id) {
		if (!userService.removeById(id)) throw getNotFoundException(id);
	}
	
	@PostMapping("/import")
	public String importStations(@RequestBody String content) {
		userService.importEntities(content);
		return "Successful imported!";
	}
	
	@GetMapping("/export")
	public Object importStations(@RequestParam(name = "download", defaultValue = "false") boolean donwload, Model model, HttpServletResponse response) throws IOException {
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
	
	private ResponseStatusException getNotFoundException(String id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " does not exist!");
	}

}
