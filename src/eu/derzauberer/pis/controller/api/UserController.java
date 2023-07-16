package eu.derzauberer.pis.controller.api;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public ListDto<UserInfoDto> getUsers(
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit
			) {
		return new ListDto<>(userService, offset, limit == -1 ? userService.size() : limit).map((user) -> modelMapper.map(user, UserInfoDto.class));
	}
	
	@GetMapping("{id}")
	public UserInfoDto getUser(@PathVariable("id") String id) {
		return userService.getById(id)
				.map((user) -> modelMapper.map(user, UserInfoDto.class))
				.orElseThrow(() -> getNotFoundException(id));
	}
	
	@PostMapping
	public UserDto setUser(@RequestBody UserEditDto user) {
		final User mappedUser = new User(user.getUsername(), user.getDisplayName(), null);
		modelMapper.map(user, mappedUser);
		userService.add(mappedUser);
		return modelMapper.map(mappedUser, UserDto.class);
	}
	
	@PutMapping
	public UserDto updateUser(@RequestBody UserEditDto user) {
		final User existingUser = userService.getById(user.getId()).orElseThrow(() -> getNotFoundException(user.getId()));
		modelMapper.map(user, existingUser);
		userService.add(existingUser);
		return modelMapper.map(existingUser, UserDto.class);
	}
	
	@DeleteMapping("{id}")
	public void deleteUser(@PathVariable("id") String id) {
		if (!userService.removeById(id)) throw getNotFoundException(id);
	}
	
	private ResponseStatusException getNotFoundException(String id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " does not exist!");
	}

}
