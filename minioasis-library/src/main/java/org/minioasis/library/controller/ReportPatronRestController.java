package org.minioasis.library.controller;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Record1;
import org.jooq.Result;
import org.minioasis.library.service.ReportService;
import org.minioasis.report.chart.ChartData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportPatronRestController {

	@Autowired
	ReportService service;
	
	@GetMapping(value = "/all.patrons.started.years", produces = "application/json")
	public List<Integer> getAllPatronStartedYears() {
		
		Result<Record1<Integer>> result = this.service.getAllPatronsStartedYears();
		
		List<Integer> years = new ArrayList<Integer>();
		
		for (Record1<Integer> r : result) {
			Integer year = r.value1();
			years.add(year);
		}
		
		return years;
	}
	
	@GetMapping(value = "/count.patrons.by.type/{from}/{to}", produces = "application/json")
	public List<ChartData> CountPatronsByType(@PathVariable(required = true) Integer from,
			@PathVariable(required = true) Integer to) {

		List<ChartData> charts = this.service.CountPatronsByTypes(from, to);
		
		return charts;
	}
	
}
