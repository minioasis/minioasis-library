package org.minioasis.library.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.ItemState;
import org.minioasis.library.domain.ItemStatus;
import org.minioasis.library.domain.Location;
import org.minioasis.library.domain.search.ItemCriteria;
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
@RequestMapping("/admin/item")
public class ItemListSearch {

	@Autowired
	private LibraryService service;
	
	@ModelAttribute("locs")
	public List<Location> populateLocations() {
		return this.service.findAllLocations();	
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
	public String search(@ModelAttribute("criteria") ItemCriteria criteria, HttpServletRequest request,
			Model model, Pageable pageable) {
		
		Page<Item> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		
		return "items";

	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String biblios(Model model, HttpServletRequest request, Pageable pageable) {

		Page<Item> page = this.service.findAllItems(pageable);

		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("criteria", new ItemCriteria());
		
		return "items";
		
	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
}
