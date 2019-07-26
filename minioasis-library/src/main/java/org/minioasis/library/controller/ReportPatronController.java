package org.minioasis.library.controller;

import java.util.List;

import org.minioasis.library.service.ReportService;
import org.minioasis.report.chart.ChartData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/report")
public class ReportPatronController {

	@Autowired
	ReportService service;

	@ModelAttribute("years")
	public List<Integer> populateYears() {
		return this.service.getAllPatronsStartedYears();	
	}
	
	@RequestMapping(value = { "/patron.statistics.form" }, method = RequestMethod.GET)
	public String form() {
		return "report.patron.statistics.form";		
	}
	
	@RequestMapping(value = { "/patrons.statistics" }, method = RequestMethod.POST)
	public String countPatronByTypes(@RequestParam(required = true) Integer from,
									@RequestParam(required = true) Integer to, Model model) {

		List<ChartData> chartDataList = this.service.CountPatronsByTypes(from, to);
		model.addAttribute("chartDataList", chartDataList);

		return "report.patron.statistics";
	}

}
