package org.minioasis.library.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.util.ItemStatusEditor;
import org.minioasis.library.domain.util.LocationEditor;
import org.minioasis.library.domain.Attachment;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.ItemState;
import org.minioasis.library.domain.ItemStatus;
import org.minioasis.library.domain.Location;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/admin/item")
public class ItemController {

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
	
	@ModelAttribute("checkeds")
	public YesNo[] populateStockChecks() {
		return YesNo.values();	
	}
	
	@ModelAttribute("actives")
	public YesNo[] populateActives() {
		return YesNo.values();	
	}
	
	@RequestMapping(value = { "/save" }, method = RequestMethod.GET)
	public String item(@RequestParam(value = "bid", required = true) long bid , Model model) {

		Item item = new Item();
		Biblio biblio = this.service.getBiblio(bid);
		
		if(biblio == null){
			return "item.not.found";
		}else{
			item.setBiblio(biblio);
			item.setFirstCheckin(LocalDate.now());
		}
			
		model.addAttribute("item", item);
		return "item.form";

	}
	
	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String itemAdd(@ModelAttribute("item") @Valid Item item, BindingResult result, Model model) {
		
		String barcode = item.getBarcode();
		
		if (result.hasErrors()) {
			
			Biblio biblio = this.service.getBiblio(item.getBiblio().getId());
			item.setBiblio(biblio);
			model.addAttribute("item", item);
			
			return "item.form";
			
		} else {

			Attachment a = this.service.getAttachment(barcode);
			if(a != null){
				result.rejectValue("barcode","error.not.unique");			
				return "item.form";
			}
			
			LocalDate firstCheckin = item.getFirstCheckin();
			LocalDate now = LocalDate.now();

			if(now.isEqual(firstCheckin)) {
				item.setFirstCheckin(now);
				item.setLastCheckin(now.atTime(00, 00, 00));				
			}else{
				item.setLastCheckin(item.getFirstCheckin().atTime(00, 00, 00));
			}

			item.setState(ItemState.IN_LIBRARY);
			item.setChecked(YesNo.N);

			try{
			
				this.service.save(item);
				
			}catch (DataIntegrityViolationException eive){
				
				result.rejectValue("barcode","error.not.unique");			
				return "item.form";
				
			}
			
		}
		return "redirect:/admin/item/" + item.getId();

	}		

	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String itemEdit(@RequestParam(value = "id", required = true) long id, Model model) {

		Item item = this.service.getItem(id);

		model.addAttribute("item", item);
		model.addAttribute("isEnabled","disabled");
		
		return "item.form";

	}

	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	public String itemEdit(@ModelAttribute("item") @Valid Item item , BindingResult result, Model model) {

		String barcode = item.getBarcode();
		
		if(result.hasErrors()){

			Biblio biblio = this.service.getBiblio(item.getBiblio().getId());
			item.setBiblio(biblio);
			model.addAttribute("item", item);
			
			return "item.form";
		} else {

			Attachment a = this.service.getAttachment(barcode);
			if(a != null){
				result.rejectValue("barcode","error.not.unique");	
				return "item.form";
			}
			
			try{

				LocalDate firstCheckin = item.getFirstCheckin();
				LocalDate now = LocalDate.now();

				if(now.isEqual(firstCheckin)) {
					item.setFirstCheckin(now);
					item.setLastCheckin(now.atTime(00, 00, 00));				
				}
					
				item.setLastCheckin(item.getFirstCheckin().atTime(00, 00, 00));
				this.service.edit(item);
				
			}catch (DataIntegrityViolationException eive){

				result.rejectValue("barcode","error.not.unique");			
				return "item.form";
				
			}
			
		}
			return "redirect:/admin/item/"+ item.getId();
			
	}	

	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {

		Locale locale = request.getLocale();

		binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, DecimalFormat.getInstance(locale), true));
		binder.registerCustomEditor(ItemStatus.class, new ItemStatusEditor(service));
		binder.registerCustomEditor(Location.class, new LocationEditor(service));

	}
	
}
