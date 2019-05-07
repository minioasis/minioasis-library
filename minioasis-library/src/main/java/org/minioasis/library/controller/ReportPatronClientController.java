package org.minioasis.library.controller;

import java.util.List;

import org.minioasis.report.chart.ChartData;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/report")
public class ReportPatronClientController {

	private final RestTemplate restTemplate;

	public ReportPatronClientController(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@RequestMapping(value = { "/count.pt" }, method = RequestMethod.GET)
	public String countPt(Model model) {

		ResponseEntity<List<ChartData>> response = restTemplate.exchange(
				"http://localhost:8080/report/count.patrons.by.type", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<ChartData>>() {
				});

		List<ChartData> chartDataList = response.getBody();

		model.addAttribute("chartDataList", chartDataList);

		return "report.count.patrons.by.type";
	}
}
