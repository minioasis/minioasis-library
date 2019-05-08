package org.minioasis.library.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Result;
import org.minioasis.library.service.ReportService;
import org.minioasis.report.chart.ChartData;
import org.minioasis.report.chart.Series;
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

		int period = 1;
		period = to - from + period;

		Result<Record3<Integer, String, Integer>> result = null;

		List<ChartData> charts = new LinkedList<ChartData>();

		for (int i = 0; i < period; i++) {

			from = from + i;

			result = this.service.CountPatronsByTypes3(from);

			if (result.size() > 0) {
				
				ChartData chartData = new ChartData();
				// set title
				chartData.setTitle(from.toString());

				List<Series> series = chartData.getSeries();

				Series s = new Series();
				String lastName = "";
				
				for (Record3<Integer, String, Integer> r : result) {

					Integer month = r.value1();
					String name = r.value2();
					Integer count = r.value3();

					String state = "";

					// set state
					if (s.getName() == null) {
						state = "NEW_SERIES";		
					}else {
						if(name.equals(lastName)) {
							state = "IN_THE_SERIES";	
						}else {
							state = "ANOTHER_SERIES";
							
						}
					}
					
					switch(state) {
						case "NEW_SERIES":		
							lastName = name;
							s.setName(name);
							s.getData()[month - 1] = count;		
							break;
							
						case "IN_THE_SERIES":	
							s.getData()[month - 1] = count;	
							break;
							
						case "ANOTHER_SERIES":
							lastName = name;
							
							series.add(s);
							s = new Series();			
							s.setName(name);
							s.getData()[month - 1] = count;
							break;				
					}

				}
				
				series.add(s);
				chartData.setSeries(series);
				charts.add(chartData);
			}
		}

		return charts;
	}
	
}
