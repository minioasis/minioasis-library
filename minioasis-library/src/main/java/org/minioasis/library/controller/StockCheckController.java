package org.minioasis.library.controller;

import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.search.StockCheckCmd;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/stock.check")
public class StockCheckController {

	@Autowired
	private LibraryService service;

	@RequestMapping(value = "/item", method = RequestMethod.GET)
	public String stockChecks(Model model) {

		model.addAttribute("cmd", new StockCheckCmd());	
		return "stockcheck.item.form";
		
	}
	
	@RequestMapping(value = { "/item" }, method = RequestMethod.POST)
	public String stockCheck(@ModelAttribute("cmd") StockCheckCmd cmd , BindingResult result, Model model) {

		String barcode = cmd.getBarcode();
		Item item = this.service.getItemFetchBiblio(barcode);

		if(item == null){		
			result.reject("info.no.item.found");		
		}else {
			if (item.getChecked().equals(YesNo.Y)) {	

				result.reject("info.item.checked");

			}else {			
				item.setChecked(YesNo.Y);
				this.service.save(item);			
				cmd.setItem(item);
			}
		}

		cmd.setBarcode(null);
		model.addAttribute("cmd", cmd);

		return "stockcheck.item.form";

	}

	@RequestMapping(value = { "/reset" }, method = RequestMethod.POST)
	public String reset() {

		this.service.stockCheck();	
		return "redirect:/admin/stock.check/reset.done";
		
	}
	
	@RequestMapping(value = { "/reset.done" }, method = RequestMethod.GET)
	public String resetDone(Model model) {

		return "stockcheck.reset.done";

	}
	
}
