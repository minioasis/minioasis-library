package org.minioasis.library.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.domain.Reservation;
import org.minioasis.library.domain.ReservationState;
import org.minioasis.library.domain.search.ReservationCriteria;
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
@RequestMapping("/admin/reservation")
public class ReservationListSearch {

	@Autowired
	private LibraryService service;
	
	@ModelAttribute("reservationStatez")
	public ReservationState[] populateReservationStates() {
		return ReservationState.values();	
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String reservations(Model model, Pageable pageable) {

		Page<Reservation> page = this.service.findAllReservations(pageable);	
		
		model.addAttribute("page", page);
		model.addAttribute("pagingType", "list");
		model.addAttribute("criteria", new ReservationCriteria());
		
		return "reservations";
		
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") ReservationCriteria criteria, HttpServletRequest request, Map<String,String> params, 
			Model model, Pageable pageable) {
		
		Page<Reservation> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("pagingType", "search");
		
		return "reservations";

	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
}
