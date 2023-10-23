package eu.derzauberer.pis.controller.web.home;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.dto.LoginDto;
import eu.derzauberer.pis.dto.PasswordDto;
import eu.derzauberer.pis.dto.UserProfileEditDto;
import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.service.UserService;

@Controller
@RequestMapping("/")
public class AuthenticationController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("login", new LoginDto());
		return "authentication/login.html";
	}
	
	@GetMapping("/password/change")
	public String changePassword(@RequestParam(name = "user") String user, Model model) {
		model.addAttribute("user", user);
		model.addAttribute("password", new PasswordDto());
		return "authentication/password_change.html";
	}
	
	@PostMapping("/password/change")
	public String changePassword(@RequestParam(name = "user") String user, PasswordDto password) {
		return "redirect:/studio";
	}
	
	@GetMapping("/password/reset")
	public String resetPassword(@RequestParam(name = "user") String user, @RequestParam(name = "token") String token, Model model) {
		model.addAttribute("user", user);
		model.addAttribute("token", token);
		model.addAttribute("password", new PasswordDto());
		return "authentication/password_reset.html";
	}
	
	@PostMapping("/password/reset")
	public String resetPassword(@RequestParam(name = "user") String user, @RequestParam(name = "token") String token, PasswordDto password) {
		return "redirect:/studio";
	}
	
	@GetMapping("/account")
	public String getSettings(Model model, Principal principal) {
		final User user = userService.getByIdOrEmail(principal.getName()).get();
		final UserProfileEditDto userProfileEditDto = modelMapper.map(user, UserProfileEditDto.class);
		
		model.addAttribute("user", userProfileEditDto);
		model.addAttribute("password", new PasswordDto());
		return "authentication/account.html";
	}
	
}
