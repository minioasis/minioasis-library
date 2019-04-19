package org.minioasis.library.controller;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.ItemDuration;
import org.minioasis.library.domain.ItemState;
import org.minioasis.library.domain.ItemStatus;
import org.minioasis.library.domain.Location;
import org.minioasis.library.domain.search.ItemCriteria;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/item")
public class ItemListSearch {

	@Autowired
	private LibraryService service;
	
	@ModelAttribute("locs")
	public List<Location> populateLocations() {
		return this.service.findAllLocations();	
	}
	
	@ModelAttribute("idurations")
	public List<ItemDuration> populateItemDurations() {
		return this.service.findAllItemDurations();	
	}
	
	@ModelAttribute("istatuz")
	public List<ItemStatus> populateItemStatus() {
		return this.service.findAllItemStatus();	
	}
	
	@ModelAttribute("istatez")
	public Collection<ItemState> populateItemStates() {
		return ItemState.getItemStates();	
	}
	
	@ModelAttribute("checkedz")
	public YesNo[] populateCheckStocks() {
		return YesNo.values();	
	}
	
	@ModelAttribute("ats")
	public YesNo[] populateActives() {
		return YesNo.values();	
	}

	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") ItemCriteria criteria, HttpServletRequest request, Map<String,String> params, 
			Model model, Pageable pageable) {
		
		Page<Item> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("pagingType", "search");
		
		return "items";

	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String biblios(Model model, Pageable pageable) {

		Page<Item> page = this.service.findAllItems(pageable);
		
		model.addAttribute("page", page);
		model.addAttribute("criteria", new ItemCriteria());
		model.addAttribute("pagingType", "list");
		
		return "items";
		
	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);

		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));	
		StringTrimmerEditor emptyTrimmer = new StringTrimmerEditor(true);
		binder.registerCustomEditor(String.class, null, emptyTrimmer);
/*		binder.registerCustomEditor(Set.class,"itemDurations", new CustomCollectionEditor(Set.class) {
	           protected Object convertElement(Object element) {
	               if (element != null) {
	                   String id = (String)element;
	                   ItemDuration itemDuration = service.getItemDuration(id);
	                   return itemDuration;
	               }
	               return null;
	           }

		});
		
		binder.registerCustomEditor(Set.class,"itemStatuz", new CustomCollectionEditor(Set.class) {
	           protected Object convertElement(Object element) {
	               if (element != null) {
	                   String id = (String)element;
	                   ItemStatus itemStatus = service.getItemStatus(id);
	                   return itemStatus;
	               }
	               return null;
	           }

		});
		
		binder.registerCustomEditor(Set.class,"locations", new CustomCollectionEditor(Set.class) {
	           protected Object convertElement(Object element) {
	               if (element != null) {
	                   String id = (String)element;
	                   Location location = service.getLocation(id);
	                   return location;
	               }
	               return null;
	           }

		});*/

	}
	
}
