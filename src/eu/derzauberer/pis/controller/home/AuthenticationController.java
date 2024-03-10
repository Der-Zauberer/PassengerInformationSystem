package eu.derzauberer.pis.controller.home;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.derzauberer.pis.dto.LoginForm;
import eu.derzauberer.pis.dto.PasswordForm;
import eu.derzauberer.pis.dto.ProfileForm;
import eu.derzauberer.pis.model.UserModel;
import eu.derzauberer.pis.service.AuthenticationService;
import eu.derzauberer.pis.service.UserService;
import eu.derzauberer.pis.util.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/")
public class AuthenticationController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationService authenticationService;

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
	public String changePassword(@RequestParam(name = "user") String username, @Valid PasswordForm passwordForm, Model model, HttpSession session) throws IOException {
		final UserModel user = userService.getById(username).orElseThrow(() -> new NotFoundException("User", username));
		if (userService.matchPassword(passwordForm.getOldPassword(), user)) {
			
			user.setPassword(userService.hashPassword(passwordForm.getNewPassword()));
			user.setPasswordChangeRequired(false);
			userService.save(user);
			
			if (session != null) {
				final SavedRequest savedRequest = (SavedRequest) session.getAttribute("passwordChangeRequest");
				if (savedRequest != null) return "redirect:" + savedRequest.getRedirectUrl();
			}
			
			return "redirect:/studio";
		} else {
			return "redirect:/password/change?user="+ username + "&error";
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
	public String resetPassword(@RequestParam(name = "user") String username, @RequestParam(name = "token") String token, @Valid PasswordForm passwordForm) {
		return "redirect:/studio";
	}
	
	@GetMapping("/account")
	public String getAccount(Model model, Principal principal) {
		final UserModel user = userService.getById(principal.getName())
				.orElseThrow(() -> new NotFoundException("user", principal.getName()));
		final ProfileForm userForm = new ProfileForm(user);
		model.addAttribute("user", userForm);
		model.addAttribute("password", new PasswordForm());
		return "authentication/account.html";
	}
	
	@PostMapping("/account")
	public String setAccount(Model model, Principal principal, HttpServletRequest request, @Valid ProfileForm userForm) {
		final UserModel user = userService.getById(principal.getName())
				.map(userForm::toUserModel)
				.orElseThrow(() -> new NotFoundException("user", principal.getName()));
		userService.save(user);
		authenticationService.updateSession(request.getSession());
		model.addAttribute("accountSuccess", true);
		return getAccount(model, principal);
	}
	
	@PostMapping("/account/password")
	public String setAccountPassword(Model model, Principal principal, @Valid PasswordForm passwordForm) {
		final UserModel user = userService.getById(principal.getName()).get();
		
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
