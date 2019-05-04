package org.minioasis.library.config;

import java.time.LocalDate;
import org.minioasis.util.LocalDateEditor;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice(basePackages = {"org.minioasis.library.controller"} )
public class GlobalControllerAdvice {
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		
		binder.registerCustomEditor(LocalDate.class, new LocalDateEditor());

		// Global String Trimmer
		StringTrimmerEditor emptyTrimmer = new StringTrimmerEditor(true);
		binder.registerCustomEditor(String.class, null, emptyTrimmer);
		
	}

}
