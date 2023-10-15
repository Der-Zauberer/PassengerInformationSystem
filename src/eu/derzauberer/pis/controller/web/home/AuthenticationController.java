package eu.derzauberer.pis.controller.web.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.dto.LoginDto;
import eu.derzauberer.pis.dto.PasswordDto;

@Controller
@RequestMapping("/")
public class AuthenticationController {

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("login", LoginDto.empty());
		return "authentication/login.html";
	}
	
	@GetMapping("/password/change")
	public String changePassword(@RequestParam(name = "user") String user, Model model) {
		model.addAttribute("user", user);
		model.addAttribute("password", PasswordDto.empty());
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
		model.addAttribute("password", PasswordDto.empty());
		return "authentication/password_reset.html";
	}
	
	@PostMapping("/password/reset")
	public String resetPassword(@RequestParam(name = "user") String user, @RequestParam(name = "token") String token, PasswordDto password) {
		return "redirect:/studio";
	}
	
}
