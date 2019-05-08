package org.minioasis.library.controller;

import java.net.URL;
import java.util.List;

import org.minioasis.library.service.RemoteAccessService;
import org.minioasis.report.chart.ChartData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/report")
public class ReportPatronClientController {

	@Autowired
	private RemoteAccessService service;

	@ModelAttribute("years")
	public List<Integer> populateYears() {
		
		URL url = service.getUrl();
		String resource = url.toString() + "/api/report/all.patrons.started.years";

		ResponseEntity<List<Integer>> response = this.service.getRestTemplate().exchange(resource , 
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Integer>>() {});
		
		List<Integer> years = response.getBody();
		
		return years;	
	}
	
	@RequestMapping(value = { "/patron.statistics.form" }, method = RequestMethod.GET)
	public String form() {
		return "report.patron.statistics.form";		
	}
	
	@RequestMapping(value = { "/patrons.statistics" }, method = RequestMethod.POST)
	public String countPatronByTypes(@RequestParam(required = true) Integer from,
									@RequestParam(required = true) Integer to, Model model) {

		URL url = service.getUrl();

		ResponseEntity<List<ChartData>> response = this.service.getRestTemplate().exchange(url.toString() + "/api/report/count.patrons.by.type/" + from + "/" + to , 
				HttpMethod.GET, null, new ParameterizedTypeReference<List<ChartData>>() {});

		List<ChartData> chartDataList = response.getBody();

		model.addAttribute("chartDataList", chartDataList);

		return "report.patron.statistics";
	}
}
