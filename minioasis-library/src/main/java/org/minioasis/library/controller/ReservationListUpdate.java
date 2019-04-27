package org.minioasis.library.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/reservation")
public class ReservationListUpdate {

	@Autowired
	private LibraryService service;
	
	@ModelAttribute("reservationStatez")
	public List<ReservationState> populateReservationStates() {
		
		List<ReservationState> states = new ArrayList<ReservationState>();
		
		states.add(ReservationState.AVAILABLE);
		states.add(ReservationState.NOTIFIED);
		
		return states;	
	}
	
	@RequestMapping(value = "/notification/list", method = RequestMethod.GET)
	public String reservations(@ModelAttribute("criteria") ReservationCriteria criteria, Model model, Pageable pageable) {

		Page<Reservation> page = this.service.findByCriteria(criteria,pageable);		
		model.addAttribute("page", page);
		model.addAttribute("pagingType", "list");
		model.addAttribute("criteria", new ReservationCriteria());
		
		return "reservations.notification";
		
	}
	
	@RequestMapping(value = { "/notification/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") ReservationCriteria criteria, HttpServletRequest request, Map<String,String> params, 
			Model model, Pageable pageable) {
		
		Page<Reservation> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("pagingType", "search");
		
		return "reservations.notification";

	}
	
	@RequestMapping(value = { "/notify/{id}" }, method = RequestMethod.GET)
	public String notify(@PathVariable("id") long id, Model model) {

		// given Date
		Date now = new Date();

		Reservation r = this.service.getReservation(id);
		
		if(r == null){
			return "item.not.found";
		}
		
		if(r.getState().equals(ReservationState.AVAILABLE)){
			r.setNotificationDate(now);
			r.setState(ReservationState.NOTIFIED);
		}else{
			return "not.in.available.state";
		}

		this.service.save(r);

		return "redirect:/reservation/notification/list?page=0&size=10&states=AVAILABLE&states=NOTIFIED&sort=availableDate,asc";

	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
}
