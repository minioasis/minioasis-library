package org.minioasis.library.controller;

import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/journalentry")
public class JournalEntryController {

	@Autowired
	private LibraryService service;
	
}
