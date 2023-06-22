package eu.derzauberer.pis.controller;

import java.util.List;

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
			@RequestParam(name = "limit", required = false, defaultValue = "-1") int limit,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset
			) {
		final List<UserInfoDto> users = userService.getList()
				.stream()
				.map(this::mapUserToUserInfoDto)
				.toList();
		return new ListDto<>(users, limit == -1 ? userService.size() : limit, offset);
	}
	
	@GetMapping("{id}")
	public UserInfoDto getUser(@PathVariable("id") String id) {
		return userService.getById(id).map(this::mapUserToUserInfoDto).orElseThrow(() -> getNotFoundException(id));
	}
	
	@PostMapping
	public UserDto setUser(UserEditDto user) {
		final User mappedUser = modelMapper.map(user, User.class);
		userService.add(mappedUser);
		return modelMapper.map(mappedUser, UserDto.class);
	}
	
	@PutMapping
	public UserDto updateUser(UserEditDto user) {
		final User existingUser = userService.getById(user.getId()).orElseThrow(() -> getNotFoundException(user.getId()));
		modelMapper.map(user, existingUser);
		return modelMapper.map(existingUser, UserDto.class);
	}
	
	@DeleteMapping("{id}")
	public void deleteUser(@PathVariable("id") String id) {
		if (!userService.removeById(id)) throw getNotFoundException(id);
	}
	
	private ResponseStatusException getNotFoundException(String id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " does not exist!");
	}
	
	private UserInfoDto mapUserToUserInfoDto(User user) {
		return modelMapper.map(user, UserInfoDto.class);
	}

}
