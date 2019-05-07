package org.minioasis.library.controller;

import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.jooq.Record3;
import org.jooq.Result;
import org.minioasis.library.domain.search.InBetweenCriteria;
import org.minioasis.library.service.ReportService;
import org.minioasis.report.chart.ChartData;
import org.minioasis.report.chart.Series;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportPatronRestController {

	@Autowired
	ReportService service;
	
	@GetMapping(value = "/count.patrons.by.type", produces = "application/json")
	public List<ChartData> CountPatronsByType(@ModelAttribute("criteria") @Valid InBetweenCriteria criteria, Model model) {
		
		Integer from = criteria.getFrom().getYear() - 1;
		Integer to = criteria.getTo().getYear();

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

					// new series
					if (s.getName() == null) {
						
						lastName = name;
						s.setName(name);
						s.getData()[month - 1] = count;
						
					}else {
						if(name.equals(lastName)) {
							
							s.getData()[month - 1] = count;
							
						}else {
							
							lastName = name;
							
							series.add(s);
							s = new Series();			
							s.setName(name);
							s.getData()[month - 1] = count;
						}
					}
				}
				
				series.add(s);
				chartData.setSeries(series);
				charts.add(chartData);
			}
		}

		// model.addAttribute("result", result);

		// return "report.count.patrons.by.type";

		return charts;
	}
	
}
