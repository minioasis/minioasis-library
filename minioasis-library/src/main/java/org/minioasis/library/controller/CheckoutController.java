package org.minioasis.library.controller;

import java.math.BigDecimal;

import org.minioasis.library.domain.Checkout;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

	@Autowired
	private LibraryService service;
	
	@RequestMapping(value = { "/edit/{id}" }, method = RequestMethod.GET)
	public String edit(@PathVariable long id, Model model) {

		Checkout c = this.service.getCheckout(id);
		
		if(c == null) {
			model.addAttribute("error", "ITEM NOT FOUND !");
			return "error";
		}
		
		model.addAttribute("checkout", c);
		return "checkout.lostdamagefine.form";
		
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	public String edit(Checkout checkout, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			return "checkout.lostdamagefine.form";
		}
		else 
		{
			Checkout existingCheckout = this.service.getCheckout(checkout.getId());
			BigDecimal lostOrDamageFineAmount = checkout.getLostOrDamageFineAmount();
			existingCheckout.setLostOrDamageFineAmount(lostOrDamageFineAmount);
			
			this.service.save(existingCheckout);
			
			return "redirect:/checkout/" + existingCheckout.getId();
			
		}
		
	}
	
	@RequestMapping(value = { "/{id}" }, method = RequestMethod.GET)
	public String view(@PathVariable("id") long id, Model model) {
		model.addAttribute("checkout", this.service.getCheckout(id));
		return "checkout";

	}
	
}
