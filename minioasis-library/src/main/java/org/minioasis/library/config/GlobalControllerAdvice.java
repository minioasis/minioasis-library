package org.minioasis.library.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice(basePackages = {"org.minioasis.library.controller"} )
public class GlobalControllerAdvice {
	
	@InitBinder
	public void dataBinding(WebDataBinder binder) {
		
		// Global Date format
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));

		// Global String Trimmer
		StringTrimmerEditor emptyTrimmer = new StringTrimmerEditor(true);
		binder.registerCustomEditor(String.class, null, emptyTrimmer);
		
	}

}
