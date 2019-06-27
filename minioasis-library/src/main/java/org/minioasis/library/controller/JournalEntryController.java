package org.minioasis.library.controller;

import java.time.LocalDate;

import org.minioasis.library.domain.Account;
import org.minioasis.library.domain.JournalEntry;
import org.minioasis.library.domain.JournalEntryLine;
import org.minioasis.library.domain.search.JournalEntryDTO;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("jedto")
@RequestMapping("/journalentry")
public class JournalEntryController {

	@Autowired
	private LibraryService service;
	
	@RequestMapping(value = { "/patronid.form" }, method = RequestMethod.GET)
	public String cardKey(Model model) {
		
		JournalEntryDTO jedto = new JournalEntryDTO();
		model.addAttribute("jedto", jedto);
		
		return "je.patronid.form";
	}	
	
	@RequestMapping(value = { "/form" }, method = RequestMethod.GET)
	public String form(@ModelAttribute("jedto") JournalEntryDTO jedto, Model model) {
		
		String cardKey = jedto.getPid();
		Account account = this.service.findByCode(cardKey);
		
		if (account == null) {
			model.addAttribute("exist", "no");
			return "je.patronid.form";
		}
		
		jedto.setJe(new JournalEntry());
		
		JournalEntryLine line = new JournalEntryLine();
		line.setAccount(account);
		jedto.setLine(line);
		
		model.addAttribute("jedto",jedto);
		
		return "je.form";

	}
	
	@RequestMapping(value = { "/form" }, method = RequestMethod.POST)
	public String submitForm(@ModelAttribute("jedto") JournalEntryDTO jedto, Model model) {
		
		String cardKey = jedto.getPid();
		Account account = this.service.findByCode(cardKey);
		
		if (account == null) {
			model.addAttribute("exist", "no");
			return "je.patronid.form";
		}
		
		JournalEntry je = jedto.getJe();
		this.service.save(je);
		Long id = je.getId();
		System.out.println("*********id***********" + id);
		
		JournalEntry done = this.service.getJournalEntry(id);
		System.out.println("*********done***********" + done);
		
		// new JournalEntry
		jedto.setJe(new JournalEntry());
		
		JournalEntryLine line = new JournalEntryLine();
		line.setAccount(account);
		jedto.setLine(line);
		
		jedto.setDone(done);

		model.addAttribute("jedto",jedto);
		
		return "je.form";

	}
	
	@RequestMapping(value = { "/add.entry" }, method = RequestMethod.POST)
	public String doubleEntry(@ModelAttribute("jedto") JournalEntryDTO jedto, Model model) {
		
		JournalEntry je = jedto.getJe();
		
		if(je.getTxnDate() == null) {
			je.setTxnDate(LocalDate.now());
		}
		
		JournalEntryLine line = jedto.getLine();

		String cardKeyName = line.getAccount().getCode();
		String cardKey = extractCode(cardKeyName);
		Account account = this.service.findByCode(cardKey);
		
		if(line.getDescription() == null && je.getDescription() != null) {
			line.setDescription(je.getDescription());
		}
		
		line.setAccount(account);
		
		jedto.addLines();
		jedto.setDone(null);
		model.addAttribute("jedto",jedto);
		
		return "je.form";

	}
	
	private String extractCode(String o) {
		if(o != null) {
			int to = o.indexOf(":") - 1;
			if(to < 0)
				return o;
			return o.substring(0, to);
		}
		return "";
	}
	
}
