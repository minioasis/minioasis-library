package org.minioasis.library.controller;

import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Result;
import org.minioasis.library.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportPatronController {

	@Autowired
	ReportService service;
	
	@RequestMapping("/count.patrons.by.type")
	public String CountPatronsByType(Model model) {
		
		Result<Record3<Integer, String, Integer>> result = this.service.CountPatronsByTypes();
		
		for (Record r : result) {
            System.out.println("**************************" + r.field("NAME"));
        }
		
		model.addAttribute("result", result);
		
		return "report.count.patrons.by.type";
	}
	
}
