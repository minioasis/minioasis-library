package org.minioasis.library.controller;

import org.minioasis.library.domain.Checkout;
import org.minioasis.library.domain.CheckoutState;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/circ/checkout")
public class CheckoutController {

	@Autowired
	private LibraryService service;
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", required = true) long id, Model model) {

		Checkout c = this.service.getCheckout(id);
		
		if(c == null) {
			model.addAttribute("error", "ITEM NOT FOUND !");
			return "error";
		}
		
		model.addAttribute("checkout", c);
		return "checkout.edit.form";
		
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	public String edit(Checkout checkout, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			return "checkout.edit.form";
		}
		else 
		{
			Checkout existingCheckout = this.service.getCheckout(checkout.getId());
			CheckoutState state = existingCheckout.getState();
			
			if (state.equals(CheckoutState.REPORTLOST) 
					|| state.equals(CheckoutState.REPORTLOST_WITH_FINE) 
					|| state.equals(CheckoutState.RETURN_WITH_DAMAGE) 
					|| state.equals(CheckoutState.RETURN_WITH_DAMAGE_AND_FINE)) {
				
				existingCheckout.setLostOrDamageFineAmount(checkout.getLostOrDamageFineAmount());
				this.service.update(existingCheckout);
				
			}
			// otherwise do nothing
			return "redirect:/admin/circ/checkout/" + existingCheckout.getId();
			
		}
		
	}
	
	@RequestMapping(value = { "/{id}" }, method = RequestMethod.GET)
	public String view(@PathVariable("id") long id, Model model) {
		
		model.addAttribute("checkout", this.service.getCheckout(id));
		return "checkout";

	}
	
}
