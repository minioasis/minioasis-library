package org.minioasis.library.controller;

import org.minioasis.library.domain.Account;
import org.minioasis.library.domain.JournalEntryLine;
import org.minioasis.library.domain.search.JournalEntryDTO;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("jedto")
@RequestMapping("/journalentry")
public class JournalEntryController {

	@Autowired
	private LibraryService service;
	
	@RequestMapping(value = { "/patronid.form" }, method = RequestMethod.GET)
	public String cardKey(Model model) {
		return "je.patronid.form";
	}
	
	@RequestMapping(value = { "/double.entry" }, method = RequestMethod.GET)
	public String doubleEntry(@RequestParam(value = "pid", required = true) String cardKey, Model model) {

		JournalEntryDTO jedto = new JournalEntryDTO();
		
		Account account = this.service.findByCode(cardKey);
		
		if (account == null) {
			model.addAttribute("exist", "no");
			return "je.patronid.form";
		}
		
		JournalEntryLine line1 = new JournalEntryLine();
		line1.setAccount(account);
		jedto.setLine1(line1);
		
		JournalEntryLine line2 = new JournalEntryLine();
		line2.setToAccount(account);
		jedto.setLine2(line2);
		
		jedto.addLines();
		
		model.addAttribute("jedto",jedto);
		model.addAttribute("pid", cardKey);
		
		return "je.form";

	}
}
