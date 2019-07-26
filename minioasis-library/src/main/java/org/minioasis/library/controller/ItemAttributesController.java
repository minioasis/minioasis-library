package org.minioasis.library.controller;

import java.util.ArrayList;
import java.util.List;

import org.minioasis.library.domain.ItemStatus;
import org.minioasis.library.domain.Location;
import org.minioasis.library.domain.search.ItemAttributes;
import org.minioasis.library.domain.validator.ItemAttributesValidator;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class ItemAttributesController {

	@Autowired
	private LibraryService service;
	
	@ModelAttribute("locations")
	public List<Location> populateLocations() {
		return this.service.findAllLocations();	
	}
	
	@ModelAttribute("itemStatuz")
	public List<ItemStatus> populateItemStatus() {
		return this.service.findAllItemStatus();	
	}
	
	@ModelAttribute("borrowables")
	public List<Boolean> populateBorrowables() {
		
		List<Boolean> trueAndFalse =  new ArrayList<Boolean>();
		trueAndFalse.add(Boolean.TRUE);
		trueAndFalse.add(Boolean.FALSE);
		
		return trueAndFalse;	
	}
	
	@ModelAttribute("reservables")
	public List<Boolean> populateReservables() {
		
		List<Boolean> trueAndFalse =  new ArrayList<Boolean>();
		trueAndFalse.add(Boolean.TRUE);
		trueAndFalse.add(Boolean.FALSE);
		
		return trueAndFalse;	
	}
	
	@RequestMapping(value = { "/item.attributes" }, method = RequestMethod.GET)
	public String showAll(Model model){
		
		model.addAttribute("attributes", new ItemAttributes());
		return "item.attributes";
		
	}
	
	// --------------- Item Status ---------------
	@RequestMapping(value = { "/item.status/save" }, method = RequestMethod.POST)
	public String addItemStatus(@ModelAttribute("attributes") ItemAttributes attributes , BindingResult result , ModelMap model) {

		ItemStatus status = attributes.getItemStatus();
		new ItemAttributesValidator().validate(attributes, result);
		
		if(result.hasErrors()){
			
			return "item.attributes";
			
		} else {
			
			try{
				
				this.service.save(status);
				
			}catch (DataIntegrityViolationException eive){
				
				result.rejectValue("itemStatus.name","error.not.unique");			
				return "item.attributes";
				
			}
			
		}
		
		return "redirect:/admin/item.attributes";
			
	}
	
	@RequestMapping(value = { "/item.status/edit/{id}" }, method = RequestMethod.GET)
	public String editItemStatus(@PathVariable("id") long id , @ModelAttribute("attributes") ItemAttributes attributes, ModelMap model) {

		ItemStatus itemStatus = this.service.getItemStatus(id);	
		attributes.setItemStatus(itemStatus);
		
		model.addAttribute("attributes", attributes);
		
		return "item.attributes";

	}
	
	@RequestMapping(value = { "/item.status/delete/{id}" }, method = RequestMethod.GET)
	public String deleteItemStatus(@PathVariable("id") long id) {

		ItemStatus status = this.service.getItemStatus(id);
		if(status != null)
			this.service.delete(status);
		
		return "redirect:/admin/item.attributes";
		
	}
	
	// --------------- Location ---------------
	@RequestMapping(value = { "/location/save" }, method = RequestMethod.POST)
	public String addLocation(@ModelAttribute("attributes") ItemAttributes attributes , BindingResult result) {

		Location location = attributes.getLocation();
		new ItemAttributesValidator().validate(attributes, result);
		
		if(result.hasErrors()){
			
			return "item.attributes";	
			
		} else {
			
			try{
				
				this.service.save(location);
				
			}catch (DataIntegrityViolationException eive){

				result.rejectValue("location.name","error.not.unique");			
				return "item.attributes";
				
			}
			
		}
		
		return "redirect:/admin/item.attributes";
			
	}
	
	@RequestMapping(value = { "/location/edit/{id}" }, method = RequestMethod.GET)
	public String editLocation(@PathVariable("id") long id, @ModelAttribute("attributes") ItemAttributes attributes, ModelMap model) {

		Location location = this.service.getLocation(id);
		attributes.setLocation(location);
		
		model.addAttribute("attributes", attributes);
		
		return "item.attributes";

	}
	
	@RequestMapping(value = { "/location/delete/{id}" }, method = RequestMethod.GET)
	public String deleteLocation(@PathVariable("id") long id) {

		Location location = this.service.getLocation(id);
		if(location != null)
			this.service.delete(location);
		
		return "redirect:/admin/item.attributes";
		
	}
	
}
