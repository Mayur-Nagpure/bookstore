package com.crni99.bookstore.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/admin-login")
	public String showLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean isAdmin = authentication != null
				&& authentication.isAuthenticated()
				&& !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)
				&& authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));

		if (isAdmin) {
			// already admin, go straight to /book
			return "redirect:/book";
		}

		// Show login page if not authenticated as ADMIN,
		// even if authenticated as USER (OAuth2)
		return "admin-login";
	}

}