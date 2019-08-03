package org.minioasis.library.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.exception.LibraryException;
import org.minioasis.library.service.AuthenticationFacade;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/member/reservation")
public class MemberReservationController {

	@Autowired
	private LibraryService service;
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@RequestMapping(value = { "/biblio/{id}" }, method = RequestMethod.GET)
	public String reserve(@PathVariable("id") long id, Model model) {

		// given Date
		LocalDateTime nowLDT = LocalDateTime.now();
		LocalDate now = nowLDT.toLocalDate();
		
		// expiry date is 1 month
		LocalDate expiryDate = now.plusDays(30);

		// patron & item
		String username = getCurrentUsername();
		Patron p = this.service.findByEntangled(username);
		if (p == null) {
			return "member/no.member.found";
		}

		String cardKey = p.getCardKey();
		Patron patron = this.service.preparingPatronForCirculation(cardKey, now);

		Biblio biblio = this.service.getBiblio(id);

		try {

			this.service.reserve(patron, biblio, nowLDT, expiryDate);
			
		}catch (LibraryException ex) {

			patron = this.service.preparingPatronForCirculation(cardKey, now);

			model.addAttribute("patron", patron);
			model.addAttribute("RESERVATION_ERRORS", ex.getAllErrors());
			
			return "member/index";
		}

		model.addAttribute("patron", patron);

		return "redirect:/member/index#reservation";

	}
	
	@RequestMapping(value = { "/cancel/{id}" }, method = RequestMethod.GET)
	public String cancelReservation(@PathVariable("id") long id, Model model) {
		
		// given Date
		LocalDateTime nowLDT = LocalDateTime.now();
		LocalDate now = nowLDT.toLocalDate();

		// patron & item
		String username = getCurrentUsername();
		Patron p = this.service.findByEntangled(username);
		if (p == null) {
			return "member/no.member.found";
		}

		String cardKey = p.getCardKey();
		Patron patron = this.service.preparingPatronForCirculation(cardKey, now);
		
		try {
			
			this.service.cancelReservation(patron, id, now);
			
		}catch (LibraryException ex) {
			
			patron = this.service.preparingPatronForCirculation(cardKey, now);

			model.addAttribute("patron", patron);
			model.addAttribute("RESERVATION_ERRORS", ex.getAllErrors());
			
			return "member/index";
		}
		
		model.addAttribute("patron", patron);

		return "redirect:/member/index#reservation";
		
	}
	
	private String getCurrentUsername() {
		Authentication authentication = authenticationFacade.getAuthentication();
		return authentication.getName();
	}
	
}
