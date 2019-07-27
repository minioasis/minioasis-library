package org.minioasis.library.config;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;

import org.minioasis.library.domain.Role;
import org.minioasis.library.service.SecurityService;
import org.minioasis.util.LocalDateEditor;
import org.minioasis.util.LocalDateTimeEditor;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice(basePackages = {"org.minioasis.library.controller"} )
public class GlobalControllerAdvice {
	
	@Resource
	private SecurityService securityService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {
		
		binder.registerCustomEditor(LocalDate.class, new LocalDateEditor());
		binder.registerCustomEditor(LocalDateTime.class, new LocalDateTimeEditor());

		// Global String Trimmer
		StringTrimmerEditor emptyTrimmer = new StringTrimmerEditor(true);
		binder.registerCustomEditor(String.class, null, emptyTrimmer);
		
		Locale locale = request.getLocale();
		binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, DecimalFormat.getInstance(locale), true));
		
		binder.registerCustomEditor(Set.class,"roles", new CustomCollectionEditor(Set.class) {
	           protected Object convertElement(Object element) {
	               if (element != null) {
	                   Long id = new Long((String)element);
	                   Role role = securityService.getRole(id);
	                   return role;
	               }
	               return null;
	           }

		});
		
	}

}
