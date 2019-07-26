package org.minioasis.library.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.minioasis.library.domain.Account;
import org.minioasis.library.domain.AccountType;
import org.minioasis.library.domain.search.AccountCriteria;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/admin/account")
public class AccountController {

	@Autowired
	private LibraryService service;

	@ModelAttribute("accountTypez")
	public AccountType[] populateAccountTypes() {
		return AccountType.values();	
	}
	
	@RequestMapping("/save")
	public String create(Model model) {
		model.addAttribute("account", new Account());
		return "account.form";
	}	
	
	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(@Valid Account account, BindingResult result, Model model) {

		if(result.hasErrors()){	
			return "account.form";			
		} else {		
			
			try{
				this.service.save(account);
			} 
			catch (DataIntegrityViolationException eive)
			{
				result.rejectValue("code","error.not.unique");			
				return "account.form";				
			}
			
			model.addAttribute("account", new Account());
			model.addAttribute("done", account);
			
			return "account.form";
			
		}			
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", required = true) long id, Model model) {

		Account account = this.service.getAccount(id);
		
		if(account == null) {
			model.addAttribute("error", "ITEM NOT FOUND !");
			return "error";
		}
		
		model.addAttribute("account", account);
		return "account.form";
		
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	public String edit(@ModelAttribute("account") @Valid Account account, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			return "account.form";
		}
		else 
		{
			try{
				this.service.save(account);
			}
			catch (DataIntegrityViolationException eive)
			{
				result.rejectValue("code","error.not.unique");		
				return "account.form";
			}
			
			model.addAttribute("done", account);
			
			return "account.form";
			
		}
		
	}
	
	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable("id") long id, Model model) {

		Account account = this.service.getAccount(id);
		if(account != null)
			this.service.deleteAccount(id);
		
		model.addAttribute("id", id);
		
		return "deleted";
		
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String groups(Model model, Pageable pageable) {

		Page<Account> page = this.service.findAllAccount(pageable);
		
		model.addAttribute("page", page);
		model.addAttribute("pagingType", "list");
		
		return "accounts";
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") AccountCriteria criteria, HttpServletRequest request, Map<String,String> params, 
			Model model, Pageable pageable) {

		Page<Account> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);

		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("page", page);
		model.addAttribute("pagingType", "search");
		
		return "accounts";

	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
}
