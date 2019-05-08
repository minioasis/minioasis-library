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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/report")
public class ReportPatronClientController {

	@Autowired
	private RemoteAccessService service;

	@RequestMapping(value = { "/count.patrons.by.type" }, method = RequestMethod.GET)
	public String countPt(Model model) {

		URL url = service.getUrl();

		ResponseEntity<List<ChartData>> response = this.service.getRestTemplate().exchange(url.toString() + "/api/report/count.patrons.by.type", 
				HttpMethod.GET, null, new ParameterizedTypeReference<List<ChartData>>() {});

		List<ChartData> chartDataList = response.getBody();

		model.addAttribute("chartDataList", chartDataList);

		return "report.count.patrons.by.type";
	}
}
