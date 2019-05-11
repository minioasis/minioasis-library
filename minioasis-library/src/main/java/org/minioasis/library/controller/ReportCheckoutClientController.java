package org.minioasis.library.controller;

import java.net.URL;
import java.util.List;

import org.minioasis.library.domain.search.CheckoutSummary;
import org.minioasis.library.service.RemoteAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/report")
public class ReportCheckoutClientController {
	
	@Autowired
	private RemoteAccessService service;
	
	@RequestMapping(value = { "/top.list.patrons.for.checkouts" }, method = RequestMethod.GET)
	public String topListPatronsForCheckouts(Model model) {

		URL url = service.getUrl();

		ResponseEntity<List<CheckoutSummary>> response = this.service.getRestTemplate().exchange(
				url.toString() + "/api/report/top.list.patrons.for.checkouts", 
				HttpMethod.GET,
				null, 
				new ParameterizedTypeReference<List<CheckoutSummary>>() {});

		List<CheckoutSummary> list = response.getBody();
		
		model.addAttribute("list", list);

		return "report.top.list.patrons.for.checkouts";
	}
}
