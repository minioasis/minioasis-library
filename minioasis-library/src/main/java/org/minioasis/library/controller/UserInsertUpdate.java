package org.minioasis.library.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.minioasis.library.domain.Role;
import org.minioasis.library.domain.User;
import org.minioasis.library.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/user")
public class UserInsertUpdate {

	@Resource
	private SecurityService securityService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@ModelAttribute("roles")
	public List<Role> populateAllRoles() {	
		return (List<Role>)this.securityService.findAllRoles(Sort.by("name"));
	}
	
	@RequestMapping(method = RequestMethod.GET, value= {"/save","/save/{id}"})
	public String save(@PathVariable Optional<Long> id, Model model) {
		
		long doneId = 0;

		if (id.isPresent()) {
			doneId = id.get();
		}
		
		User done = null;
		
		Optional<User> optioanlUser = securityService.getUser(doneId);
		if (optioanlUser.isPresent()) {
			done = optioanlUser.get();
		}
		
		model.addAttribute("done", done);
		model.addAttribute("user", new User());
		
		return "security/user.form";
	}
	
	@RequestMapping(method = RequestMethod.POST, value= {"/save","/save/{id}"})
	public String save(@PathVariable Optional<Long> id, @Valid User user, BindingResult result) {
		
		if (result.hasErrors()) {
			return "security/user.form";
		} else {

			String password = user.getPassword();
			String encodedPassword = passwordEncoder.encode(password);	
			user.setPassword(encodedPassword);
			
			try{
				securityService.add(user);
			}
			catch (DataIntegrityViolationException eive) 
			{
				User u = this.securityService.findByUsername(user.getUsername());		
				if(u != null)
					result.rejectValue("username","error.not.unique");
				
				return "security/user.form";
			}
				
			return "redirect:/admin/user/save/" + user.getId();
			
		}
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", required=true) long id, Model model) {

		Optional<User> user = this.securityService.getUser(id);

		if (!user.isPresent()) {
			model.addAttribute("error", "ITEM NOT FOUND !");
			return "error";
		}

		model.addAttribute("user", user);
		
		return "security/user.form";

	}
	
	@RequestMapping(method = RequestMethod.POST, value= "/edit")
	public String edit(@Valid User user, BindingResult result) {

		if (result.hasErrors()) {
			return "security/user.form";
		} else {

			String password = user.getPassword();
			String encodedPassword = passwordEncoder.encode(password);	
			user.setPassword(encodedPassword);
			
			try{
				securityService.add(user);
			}
			catch (DataIntegrityViolationException eive) 
			{
				User u = this.securityService.findByUsername(user.getUsername());		
				if(u != null)
					result.rejectValue("username","error.not.unique");
				
				return "security/user.form";
			}
				
			return "redirect:/admin/user/save/" + user.getId();
			
		}
	}
	
}
