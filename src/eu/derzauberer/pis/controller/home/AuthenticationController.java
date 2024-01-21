package eu.derzauberer.pis.controller.home;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.converter.FormConverter;
import eu.derzauberer.pis.service.AuthenticationService;
import eu.derzauberer.pis.service.UserService;
import eu.derzauberer.pis.structure.form.LoginForm;
import eu.derzauberer.pis.structure.form.PasswordForm;
import eu.derzauberer.pis.structure.form.ProfileForm;
import eu.derzauberer.pis.structure.model.User;
import eu.derzauberer.pis.util.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class AuthenticationController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private FormConverter<User, ProfileForm> profileFormConverter;

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("login", new LoginForm());
		return "authentication/login.html";
	}
	
	@GetMapping("/password/change")
	public String changePassword(@RequestParam(name = "user") String username, Model model) {
		model.addAttribute("user", username);
		model.addAttribute("password", new PasswordForm());
		return "authentication/password_change.html";
	}
	
	@PostMapping("/password/change")
	public String changePassword(@RequestParam(name = "user") String username, PasswordForm passwordForm, Model model) {
		final User user = userService.getById(username).orElseThrow(() -> new NotFoundException("User", username));
		if (userService.matchPassword(passwordForm.getOldPassword(), user)) {
			user.setPassword(userService.hashPassword(passwordForm.getNewPassword()));
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
		model.addAttribute("password", new PasswordForm());
		return "authentication/password_reset.html";
	}
	
	@PostMapping("/password/reset")
	public String resetPassword(@RequestParam(name = "user") String username, @RequestParam(name = "token") String token, PasswordForm passwordForm) {
		return "redirect:/studio";
	}
	
	@GetMapping("/account")
	public String getAccount(Model model, Principal principal) {
		final User user = userService.getById(principal.getName()).get();
		final ProfileForm userForm = profileFormConverter.convertToForm(user);
		model.addAttribute("user", userForm);
		model.addAttribute("password", new PasswordForm());
		return "authentication/account.html";
	}
	
	@PostMapping("/account")
	public String setAccount(Model model, Principal principal, HttpServletRequest request, ProfileForm userForm) {
		final User user = userService.getById(principal.getName()).get();
		profileFormConverter.convertToModel(user, userForm);
		userService.save(user);
		authenticationService.updateSession(request.getSession());
		model.addAttribute("accountSuccess", true);
		return getAccount(model, principal);
	}
	
	@PostMapping("/account/password")
	public String setAccountPassword(Model model, Principal principal, PasswordForm passwordForm) {
		final User user = userService.getById(principal.getName()).get();
		if (userService.matchPassword(passwordForm.getOldPassword(), user)) {
			user.setPassword(userService.hashPassword(passwordForm.getNewPassword()));
			userService.save(user);
			model.addAttribute("passwordSuccess", true);
		} else {
			model.addAttribute("passwordError", true);
		}
		return getAccount(model, principal);
	}
	
}
