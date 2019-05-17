package org.minioasis.library.controller;

import java.util.List;

import org.minioasis.library.domain.search.CheckoutPatronCriteria;
import org.minioasis.library.domain.search.TopCheckoutPatronsSummary;
import org.minioasis.library.domain.search.TopPopularBooksCriteria;
import org.minioasis.library.domain.search.TopPopularBooksSummary;
import org.minioasis.library.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportCheckoutRestController {

	@Autowired
	ReportService service;
	
	@PostMapping(value = "/top.list.patrons.for.checkouts", produces = "application/json")
	public List<TopCheckoutPatronsSummary> topListPatronsForCheckouts(@RequestBody CheckoutPatronCriteria criteria) {
		
		List<TopCheckoutPatronsSummary> list= this.service.topListPatronsForCheckouts(criteria);	
		
		return list;

	}
	
	@PostMapping(value = "/top.popular.books", produces = "application/json")
	public List<TopPopularBooksSummary> TopPopularBooks(@RequestBody TopPopularBooksCriteria criteria) {
		
		List<TopPopularBooksSummary> list= this.service.topPopularBooks(criteria);	
		
		return list;

	}

/*	{
		 "fields":[
		           {"schema":"PUBLIC","table":"p","name":"CARD_KEY","type":"CHAR"},
		           {"schema":"PUBLIC","table":"p","name":"NAME","type":"VARCHAR"},
		           {"schema":"PUBLIC","table":"p","name":"NAME2","type":"VARCHAR"},
		           {"schema":"PUBLIC","table":"p","name":"START_DATE","type":"DATE"},
		           {"name":"count","type":"INTEGER"}],
		 "records":[
		            ["11111","Moan Ah Meng","Mr. Moon","2019-01-03",1],
		            ["22222","Moan Wai Loong","Mr. Loong","2019-05-01",1],
		            ["44444","R.P. Feynman2","Prof Feynman2","2018-07-15",1]
		           ]
	}
	*/
	
/*	@GetMapping(value = "/top.list.patrons.for.checkouts", produces = "application/json")
	public String topListPatronsForCheckouts() {
		
		String result = this.service.topListPatronsForCheckouts_JSON();
		
		return result;
	}*/
}
