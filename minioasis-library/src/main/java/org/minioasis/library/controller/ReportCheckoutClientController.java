package org.minioasis.library.controller;

import java.net.URL;
import java.util.List;

import org.minioasis.library.domain.CheckoutState;
import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.PatronType;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.search.CheckoutPatronCriteria;
import org.minioasis.library.domain.search.TopCheckoutPatronsSummary;
import org.minioasis.library.domain.search.TopPopularBooksCriteria;
import org.minioasis.library.domain.search.TopPopularBooksSummary;
import org.minioasis.library.service.LibraryService;
import org.minioasis.library.service.RemoteAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportCheckoutClientController {
	
	@Autowired
	private RemoteAccessService service;
	
	@Autowired
	private LibraryService libraryService;

	@ModelAttribute("ats")
	public YesNo[] populateActives() {
		return YesNo.values();
	}
	
	@ModelAttribute("gps")
	public List<Group> populateGroups() {
		return this.libraryService.findAllGroups();	
	}
	
	@ModelAttribute("pts")
	public List<PatronType> populatePatronTypes() {
		return this.libraryService.findAllPatronTypes(Sort.by("name").ascending());	
	}
	
	@ModelAttribute("checkoutStatez")
	public CheckoutState[] populateCheckoutState() {
		return CheckoutState.values();	
	}
	
	@GetMapping("/top.list.patrons.for.checkouts.form")
	public String topListPatronsForCheckoutsForm(@ModelAttribute("criteria") CheckoutPatronCriteria criteria) {
		return "report.top.list.patrons.for.checkouts.form";
	}
	
	@PostMapping("/top.list.patrons.for.checkouts")
	public String topListPatronsForCheckouts(@ModelAttribute("criteria") CheckoutPatronCriteria criteria, Model model) {
		
		HttpEntity<CheckoutPatronCriteria> request = new HttpEntity<>(criteria);
		
		URL url = service.getUrl();

		ResponseEntity<List<TopCheckoutPatronsSummary>> response = this.service.getRestTemplate().exchange(
				url.toString() + "/api/report/top.list.patrons.for.checkouts", 
				HttpMethod.POST,
				request, 
				new ParameterizedTypeReference<List<TopCheckoutPatronsSummary>>() {});


		List<TopCheckoutPatronsSummary> list = response.getBody();
		
		model.addAttribute("list", list);

		return "report.top.list.patrons.for.checkouts";
	}
	
	@GetMapping("/top.popular.books")
	public String topPopularBooksForm(@ModelAttribute("criteria") TopPopularBooksCriteria criteria) {
		return "report.top.popular.books.form";
	}
	
	@PostMapping("/top.popular.books")
	public String topPopularBooks(@ModelAttribute("criteria") TopPopularBooksCriteria criteria, Model model) {
		
		HttpEntity<TopPopularBooksCriteria> request = new HttpEntity<>(criteria);
		
		URL url = service.getUrl();

		ResponseEntity<List<TopPopularBooksSummary>> response = this.service.getRestTemplate().exchange(
				url.toString() + "/api/report/top.popular.books", 
				HttpMethod.POST,
				request, 
				new ParameterizedTypeReference<List<TopPopularBooksSummary>>() {});


		List<TopPopularBooksSummary> list = response.getBody();
		
		model.addAttribute("list", list);

		return "report.top.popular.books";
	}
}
