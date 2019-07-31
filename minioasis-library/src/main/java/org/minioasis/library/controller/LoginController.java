package org.minioasis.library.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	@Autowired
	AuthenticationTrustResolver authenticationTrustResolver;
	
	  @RequestMapping(value = "/login", method = RequestMethod.GET)
	  public String loginPage(){	 
		  return "login";
	  }

	  // actually you no need the method below, spring security will POST the username & password
	  // if you submit the form to "/login" ! it won't go through the method below !!!
/*    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginForm(){
		if (isCurrentAuthenticationAnonymous()) {
			return "login";		
	    } else {
	    	return "security/index";  
	    }
    }*/
    
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
    	
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        
        //You can redirect wherever you want, but generally it's a good practice to show login screen again.
        return "redirect:/login?logout";
    }
    

	// access-denied redirect.
	@RequestMapping(value = "/access.denied", method = RequestMethod.GET)
	public String accessDeniedPage(Model model) {
		
		model.addAttribute("loggedinuser", getPrincipal());
		return "access.denied";
		
	}
	
	// returns the principal[username] of logged-in user.
	private String getPrincipal(){
		
		String userName = null;
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails)principal).getUsername();
		} else {
			userName = principal.toString();
		}
		
		return userName;
	}
    
/*	// returns true if users is already authenticated [logged-in], else false.
	private boolean isCurrentAuthenticationAnonymous() {
		
	    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    return authenticationTrustResolver.isAnonymous(authentication);
	    
	}*/
	
}
