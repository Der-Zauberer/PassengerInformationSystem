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

import eu.derzauberer.pis.dto.ListDto;
import eu.derzauberer.pis.dto.UserDto;
import eu.derzauberer.pis.dto.UserEditDto;
import eu.derzauberer.pis.dto.UserInfoDto;
import eu.derzauberer.pis.entity.User;
import eu.derzauberer.pis.service.UserService;
import eu.derzauberer.pis.util.Collectable;
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
	public ListDto<UserInfoDto> getUsers(
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		final boolean hasSearch = search != null && !search.isBlank();
		final Collectable<User> collectable = hasSearch ? userService.search(search) : userService;
		return collectable.map((user) -> modelMapper.map(user, UserInfoDto.class)).getList(offset, limit == -1 ? collectable.size() : limit);
	}
	
	@GetMapping("{id}")
	public UserInfoDto getUser(@PathVariable("id") String id) {
		return userService.getById(id)
				.map((user) -> modelMapper.map(user, UserInfoDto.class))
				.orElseThrow(() -> getNotFoundException(id));
	}
	
	@PostMapping
	public UserDto setUser(@RequestBody UserEditDto user) {
		final User mappedUser = new User(user.getId(), user.getName());
		modelMapper.map(user, mappedUser);
		userService.save(mappedUser);
		return modelMapper.map(mappedUser, UserDto.class);
	}
	
	@PutMapping
	public UserDto updateUser(@RequestBody UserEditDto user) {
		final User existingUser = userService.getById(user.getId()).orElseThrow(() -> getNotFoundException(user.getId()));
		modelMapper.map(user, existingUser);
		userService.save(existingUser);
		return modelMapper.map(existingUser, UserDto.class);
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
