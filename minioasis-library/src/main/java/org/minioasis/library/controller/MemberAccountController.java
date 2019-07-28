package org.minioasis.library.controller;

import java.time.LocalDate;
import java.util.Date;

import org.minioasis.library.domain.Patron;
import org.minioasis.library.service.AuthenticationFacade;
import org.minioasis.library.service.HolidayCalculationStrategy;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class MemberAccountController {

	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
	@Autowired
	private AuthenticationFacade authenticationFacade;
	@Autowired
	private LibraryService service;
	@Autowired
	private HolidayCalculationStrategy strategy;
	
	@RequestMapping("/index")
	public String userIndex(Model model) {

		final LocalDate now = LocalDate.now();
		String username = getCurrentUsername();

		Patron p = this.service.findByEntangled(username);
		if (p != null) {

			Patron patron = this.service.preparingPatronForCirculation(p.getCardKey(), now);
			
			model.addAttribute("patron", patron);

			return "pub/index";
		} else {
			return "pub/no.user.attached";
		}
	}
	
	private String getCurrentUsername() {
		Authentication authentication = authenticationFacade.getAuthentication();
		return authentication.getName();
	}
	
}
