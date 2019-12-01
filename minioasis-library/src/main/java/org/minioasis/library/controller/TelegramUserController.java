package org.minioasis.library.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.domain.TelegramUser;
import org.minioasis.library.domain.search.TelegramUserCriteria;
import org.minioasis.library.service.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/admin/telegram.user")
public class TelegramUserController {

	@Autowired
	private TelegramService service;
	
	@ModelAttribute("truefalse")
	public List<Boolean> populateBooleans() {
		
		List<Boolean> list =  new ArrayList<Boolean>();
		list.add(Boolean.TRUE);
		list.add(Boolean.FALSE);

		return list;
	}
	
	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable("id") long id, Model model) {

		TelegramUser user = this.service.getTelegramUser(id);
		if(user != null)
			this.service.deleteTelegramUser(id);
		
		model.addAttribute("id", id);
		return "deleted";
		
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String telegramUsers(Model model, HttpServletRequest request, Pageable pageable) {

		Page<TelegramUser> page = this.service.findAllTelegramUsers(pageable);

		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);

		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("criteria", new TelegramUserCriteria());
		
		return "telegram.users";
		
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") TelegramUserCriteria criteria, HttpServletRequest request,
			Model model, Pageable pageable) {

		Page<TelegramUser> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);

		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
	
		return "telegram.users";

	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
}
