package org.minioasis.library.controller;

import java.time.LocalDate;
import java.util.List;

import org.minioasis.library.domain.Checkout;
import org.minioasis.library.domain.CheckoutState;
import org.minioasis.library.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/report")
public class ReportCheckoutController {
	
	@Autowired
	ReportService service;
	
	@RequestMapping(value = "/overdue", method = RequestMethod.GET)
	public String overdue(Model model, Pageable pageable) {
		
		LocalDate now = LocalDate.now();
		List<CheckoutState> states = CheckoutState.getCheckouts();
		
		Page<Checkout> page = this.service.findAllOverDueOrderByDueDateCardKey(states, now, pageable);

		model.addAttribute("page", page);

		return "report.overdue";
		
	}
	
	@RequestMapping(value = "/overdue.orderby.group.patrontype", method = RequestMethod.GET)
	public String overdueOrderByGroupPatronType(Model model, Pageable pageable) {
		
		LocalDate now = LocalDate.now();
		List<CheckoutState> states = CheckoutState.getCheckouts();
		
		Page<Checkout> page = this.service.findAllOverDueOrderByGroupPatronTypeDueDateCardKey(states, now, pageable);
		
		model.addAttribute("page", page);

		return "report.overdue.orderby.group.patrontype";
		
	}

	@RequestMapping(value = "/fines.pending", method = RequestMethod.GET)
	public String returnWithFines(Model model, Pageable pageable) {
		
		LocalDate now = LocalDate.now();	
		List<CheckoutState> states = CheckoutState.getReturnWithFines();
		
		Page<Checkout> page = this.service.findAllOverDueOrderByDueDateCardKey(states, now, pageable);
		
		List<Checkout> checkouts = page.getContent();
		for(Checkout c : checkouts) {
			c.preparingCheckoutOn(now);
		}

		model.addAttribute("page", page);

		return "report.fines.pending";
		
	}
	
	@RequestMapping(value = "/fines.pending.orderby.group.patrontype", method = RequestMethod.GET)
	public String returnWithFinesOrderByGroupPatronType(Model model, Pageable pageable) {
		
		LocalDate now = LocalDate.now();	
		List<CheckoutState> states = CheckoutState.getReturnWithFines();
		
		Page<Checkout> page = this.service.findAllOverDueOrderByGroupPatronTypeDueDateCardKey(states, now, pageable);
		
		List<Checkout> checkouts = page.getContent();
		for(Checkout c : checkouts) {
			c.preparingCheckoutOn(now);
		}

		model.addAttribute("page", page);

		return "report.fines.pending.orderby.group.patrontype";
		
	}
	
	@RequestMapping(value = "/reportlost", method = RequestMethod.GET)
	public String reportLost(Model model, Pageable pageable) {
		
		List<CheckoutState> states = CheckoutState.getReportLost();
		Page<Checkout> page = this.service.findAllOverDueOrderByDueDateCardKey(states, LocalDate.now(), pageable);
		
		model.addAttribute("page", page);

		return "report.reportlost";
		
	}
	
	@RequestMapping(value = "/reportlost.orderby.group.patrontype", method = RequestMethod.GET)
	public String reportLostOrderByGroupPatronType(Model model, Pageable pageable) {
		
		List<CheckoutState> states = CheckoutState.getReportLost();
		Page<Checkout> page = this.service.findAllOverDueOrderByDueDateCardKey(states, LocalDate.now(), pageable);
		
		model.addAttribute("page", page);

		return "report.reportlost.orderby.group.patrontype";
		
	}
	
}
