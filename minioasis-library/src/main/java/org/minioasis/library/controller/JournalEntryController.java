package org.minioasis.library.controller;

import java.time.LocalDate;

import org.minioasis.library.domain.Account;
import org.minioasis.library.domain.DataType;
import org.minioasis.library.domain.FormData;
import org.minioasis.library.domain.JournalEntry;
import org.minioasis.library.domain.JournalEntryLine;
import org.minioasis.library.domain.search.JournalEntryDTO;
import org.minioasis.library.domain.validator.JournalEntryLineValidator;
import org.minioasis.library.domain.validator.JournalEntryValidator;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("jedto")
@RequestMapping("/admin/journalentry")
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
	
	@RequestMapping(value = { "/add.entry" }, method = RequestMethod.POST)
	public String doubleEntry(@ModelAttribute("jedto") JournalEntryDTO jedto, BindingResult result, Model model) {
		
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
		
		new JournalEntryLineValidator().validate(line, result);
		if(result.hasErrors()) {
			return "je.form";
		}
		
		jedto.addLines();
		jedto.setDone(null);
		model.addAttribute("jedto",jedto);
		
		return "je.form";

	}
	
	
	
	@RequestMapping(value = { "/form" }, method = RequestMethod.POST)
	public String submitForm(@ModelAttribute("jedto") JournalEntryDTO jedto, BindingResult result, Model model) {
		
		String cardKey = jedto.getPid();
		Account account = this.service.findByCode(cardKey);
		
		if (account == null) {
			model.addAttribute("exist", "no");
			return "je.patronid.form";
		}
		
		JournalEntry je = jedto.getJe();
		
		new JournalEntryValidator().validate(je, result);
		if(result.hasErrors()) {
			return "je.form";
		}
		
		this.service.save(je);
		this.service.save(new FormData(je.getDescription(), DataType.JOURNAL_ENTRY_DESP));
		Long id = je.getId();
		
		JournalEntry done = this.service.getJournalEntry(id);
		
		// new JournalEntry
		jedto.setJe(new JournalEntry());
		
		JournalEntryLine line = new JournalEntryLine();
		line.setAccount(account);
		jedto.setLine(line);
		
		jedto.setDone(done);

		model.addAttribute("jedto",jedto);
		
		return "je.form";

	}
	
	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable("id") long id, Model model) {

		JournalEntry je = this.service.getJournalEntry(id);
		if(je != null)
			this.service.deleteJournalEntry(id);
		
		return "redirect:/admin/journalentry/list?page=0&size=60&sort=txnDate,desc";
		
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
