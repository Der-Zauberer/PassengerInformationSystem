package eu.derzauberer.pis.controller.web.home;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import eu.derzauberer.pis.dto.LoginDto;
import eu.derzauberer.pis.dto.PasswordDto;
import eu.derzauberer.pis.dto.UserProfileEditDto;
import eu.derzauberer.pis.entity.User;
import eu.derzauberer.pis.service.AuthenticationService;
import eu.derzauberer.pis.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class AuthenticationController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("login", new LoginDto());
		return "authentication/login.html";
	}
	
	@GetMapping("/password/change")
	public String changePassword(@RequestParam(name = "user") String username, Model model) {
		model.addAttribute("user", username);
		model.addAttribute("password", new PasswordDto());
		return "authentication/password_change.html";
	}
	
	@PostMapping("/password/change")
	public String changePassword(@RequestParam(name = "user") String username, PasswordDto passwordDto, Model model) {
		final User user = userService.getById(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Station with id " + username + " does not exist!"));
		if (userService.matchPassword(passwordDto.getOldPassword(), user)) {
			user.setPassword(userService.hashPassword(passwordDto.getNewPassword()));
			userService.save(user);
			return "redirect:/studio";
		} else {
			model.addAttribute("passwordError", true);
			return changePassword(username, model);
		}
	}
	
	@GetMapping("/password/reset")
	public String resetPassword(@RequestParam(name = "user") String username, @RequestParam(name = "token") String token, Model model) {
		model.addAttribute("user", username);
		model.addAttribute("token", token);
		model.addAttribute("password", new PasswordDto());
		return "authentication/password_reset.html";
	}
	
	@PostMapping("/password/reset")
	public String resetPassword(@RequestParam(name = "user") String username, @RequestParam(name = "token") String token, PasswordDto passwordDto) {
		return "redirect:/studio";
	}
	
	@GetMapping("/account")
	public String getAccount(Model model, Principal principal) {
		final User user = userService.getById(principal.getName()).get();
		final UserProfileEditDto userDto = modelMapper.map(user, UserProfileEditDto.class);
		model.addAttribute("user", userDto);
		model.addAttribute("password", new PasswordDto());
		return "authentication/account.html";
	}
	
	@PostMapping("/account")
	public String setAccount(Model model, Principal principal, HttpServletRequest request, UserProfileEditDto userDto) {
		final User user = userService.getById(principal.getName()).get();
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		userService.save(user);
		authenticationService.updateSession(request.getSession());
		model.addAttribute("accountSuccess", true);
		return getAccount(model, principal);
	}
	
	@PostMapping("/account/password")
	public String setAccountPassword(Model model, Principal principal, PasswordDto passwordDto) {
		final User user = userService.getById(principal.getName()).get();
		if (userService.matchPassword(passwordDto.getOldPassword(), user)) {
			user.setPassword(userService.hashPassword(passwordDto.getNewPassword()));
			userService.save(user);
			model.addAttribute("passwordSuccess", true);
		} else {
			model.addAttribute("passwordError", true);
		}
		return getAccount(model, principal);
	}
	
}
