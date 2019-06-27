package org.minioasis.library.controller;

import java.util.List;

import org.minioasis.library.domain.Account;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/account")
public class AccountRestController {

	@Autowired
	private LibraryService service;
	
	@RequestMapping(value = "/like", method = RequestMethod.GET)
	public @ResponseBody List<Account> findAccounts(@RequestParam("code") String code) {

		if(code != null && !code.isEmpty()) {
			List<Account> accounts = this.service.findByCodeContaining(code);
			return accounts;
		}
	
		return null;
	}
}
