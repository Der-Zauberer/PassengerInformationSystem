package eu.derzauberer.pis.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.derzauberer.pis.dto.ListDto;
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
	public ListDto<User> getUsers(
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset
			) {
		return new ListDto<>(userService.getUsers(), limit == -1 ? userService.size() : limit, offset);
	}
	
	@GetMapping("{id}")
	public User getUser(@PathVariable("id") String id) {
		return userService.getById(id).orElseThrow(() -> getNotFoundException(id));
	}
	
	@PostMapping
	public User setUser(User user) {
		userService.add(user);
		return user;
	}
	
	@PutMapping
	public User updateUser(User user) {
		final User existingUser = userService.getById(user.getId()).orElseThrow(() -> getNotFoundException(user.getId()));
		modelMapper.map(user, existingUser);
		return existingUser;
	}
	
	@DeleteMapping("{id}")
	public void deleteUser(@PathVariable("id") String id) {
		if (!userService.removeById(id)) throw getNotFoundException(id);
	}
	
	private ResponseStatusException getNotFoundException(String id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " does not exist!");
	}

}
