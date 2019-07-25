package org.minioasis.library.controller;

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
import org.minioasis.library.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportTopCheckoutController {
	
	@Autowired
	ReportService service;
	
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

	@PostMapping(path = "/top.list.patrons.for.checkouts")
	public String topListPatronsForCheckouts(@ModelAttribute("criteria") CheckoutPatronCriteria criteria, Model model) {
		
		List<TopCheckoutPatronsSummary> list = this.service.topListPatronsForCheckouts(criteria);
		model.addAttribute("list", list);

		return "report.top.list.patrons.for.checkouts";
	}
	
	@GetMapping("/top.popular.books.form")
	public String topPopularBooksForm(@ModelAttribute("criteria") TopPopularBooksCriteria criteria) {
		return "report.top.popular.books.form";
	}
	
	@PostMapping("/top.popular.books")
	public String topPopularBooks(@ModelAttribute("criteria") TopPopularBooksCriteria criteria, Model model) {
		
		List<TopPopularBooksSummary> list= this.service.topPopularBooks(criteria);	
		model.addAttribute("list", list);

		return "report.top.popular.books";
	}
}
