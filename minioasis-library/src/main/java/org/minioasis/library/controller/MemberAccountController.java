package org.minioasis.library.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.domain.Checkout;
import org.minioasis.library.domain.CheckoutState;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.search.CheckoutCriteria;
import org.minioasis.library.exception.LibraryException;
import org.minioasis.library.service.AuthenticationFacade;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/member")
public class MemberAccountController {

	@Autowired
	private AuthenticationFacade authenticationFacade;
	@Autowired
	private LibraryService service;
	
	@RequestMapping("/index")
	public String userIndex(Model model) {

		final LocalDate now = LocalDate.now();
		String username = getCurrentUsername();

		Patron p = this.service.findByEntangled(username);
		if (p != null) {

			Patron patron = this.service.preparingPatronForCirculation(p.getCardKey(), now);
			
			model.addAttribute("patron", patron);

			return "member/index";
		} else {
			return "member/no.member.found";
		}
	}
	
	@RequestMapping(value = "/histories", method = RequestMethod.GET)
	public String histories(Model model) {
		
		String username = getCurrentUsername();
		
		Patron patron = this.service.findByEntangled(username);
		if (patron == null) {
			return "member/no.member.found";
		}
		
		model.addAttribute("patron", patron);
		
		CheckoutState[] cStates = {
				CheckoutState.CHECKOUT,
				CheckoutState.RENEW
		};
		
		Page<Checkout> page = this.service.findAllCheckouts(username, Arrays.asList(cStates), PageRequest.of(0, 15, Sort.by("checkoutDate").descending()));	
		
		model.addAttribute("page", page);
		model.addAttribute("criteria", new CheckoutCriteria());
		
		return "member/histories";

	}
	
	@RequestMapping(value = { "/history/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") CheckoutCriteria criteria, HttpServletRequest request, Map<String,String> params, 
			Model model, Pageable pageable) {

		String username = getCurrentUsername();
		Patron patron = this.service.findByEntangled(username);
		if (patron == null) {
			return "member/no.member.found";
		}
		
		model.addAttribute("patron", patron);
		
		Page<Checkout> page = this.service.findByCriteria(criteria, pageable);

		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("pagerType", "search");
		
		return "member/histories";

	}
	
	@RequestMapping(value = { "/member/renew/{barcode}" }, method = RequestMethod.GET)
	public String renew(@PathVariable("barcode") String barcode, Model model) {

		// given Date
		final LocalDate now = LocalDate.now();

		// patron & item
		String username = getCurrentUsername();
		Patron p = this.service.findByEntangled(username);
		if (p == null) {
			return "member/no.member.found";
		}

		String cardKey = p.getCardKey();
		Patron patron = this.service.preparingPatronForCirculation(cardKey, now);

		Item item = this.service.getItemForCheckout(barcode);	
		
		if (item == null) {
			model.addAttribute("RENEW_ERRORS", "item not found");
			model.addAttribute("patron", patron);
			
			return "member/index";
		}

		try {

			this.service.renew(patron, item, now);

		} catch (LibraryException ex) {
			
			patron = this.service.preparingPatronForCirculation(cardKey, now);

			model.addAttribute("patron", patron);
			model.addAttribute("RENEW_ERRORS", ex.getAllErrors());
			
			return "member/index";

		}

		model.addAttribute("patron", patron);

		return "redirect:/member/index";

	}
	
	@RequestMapping(value = { "/member/reservation/cancel/{id}" }, method = RequestMethod.GET)
	public String cancelReservation(@PathVariable("id") long id, Model model) {
		
		// given Date
		final LocalDate now = LocalDate.now();

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

		return "redirect:/member/index";
		
	}
	
	private String getCurrentUsername() {
		Authentication authentication = authenticationFacade.getAuthentication();
		return authentication.getName();
	}
	
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
}
