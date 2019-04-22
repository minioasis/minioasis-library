package org.minioasis.library.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.domain.Checkout;
import org.minioasis.library.domain.CheckoutState;
import org.minioasis.library.domain.search.CheckoutCriteria;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/checkout")
public class CheckoutListSearch {

	@Autowired
	private LibraryService service;
	
	@ModelAttribute("checkoutStatez")
	public CheckoutState[] populateCheckoutState() {
		return CheckoutState.values();	
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String checkouts(Model model, Pageable pageable) {

		Page<Checkout> page = this.service.findAllCheckouts(pageable);		
		model.addAttribute("page", page);
		model.addAttribute("criteria", new CheckoutCriteria());
		return "checkouts";
		
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") CheckoutCriteria criteria, HttpServletRequest request, Map<String,String> params, 
			Model model, Pageable pageable) {

		Page<Checkout> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("pagerType", "search");
		
		return "checkouts";

	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);

		return uc.toUriString();
	}
	
}
