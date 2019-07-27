package org.minioasis.library.controller;

import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.domain.User;
import org.minioasis.library.service.SecurityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/admin/user")
public class UserListSearch {

	@Resource
	private SecurityService securityService;
	
	
	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable("id") long id, Model model) {

		Optional<User> user = this.securityService.getUser(id);
		if (user.isPresent())
			this.securityService.deleteUser(id);

		model.addAttribute("id", id);
		
		return "deleted";
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@RequestParam(value = "username", required = true) String username, 
			HttpServletRequest request, Model model, Pageable pageable) {
		
		if(username != null){
			Page<User> page = this.securityService.findAllUsersByUsernameContaining(username, pageable);
			
			String next = buildUri(request, page.getNumber() + 1);
			String previous = buildUri(request, page.getNumber() - 1);

			model.addAttribute("page", page);
			model.addAttribute("next", next);
			model.addAttribute("previous", previous);
			model.addAttribute("pagingType", "search");
		}

		return "security/users";

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String users(Model model, Pageable pageable) {

		Page<User> page = this.securityService.findAllUsers(pageable);
		
		model.addAttribute("page", page);
		model.addAttribute("pagingType", "list");
		
		return "security/users";

	}

	private String buildUri(HttpServletRequest request, int page) {
		
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request).replaceQueryParam("page", "{id}").build()
				.expand(page);
		return uc.toUriString();
	}
	
}
