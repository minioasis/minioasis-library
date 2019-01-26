package org.minioasis.library.controller;

import java.util.Locale;

import org.minioasis.library.config.i18nConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class HomeController {
	
	@Autowired
	private i18nConfig i18n;

	@GetMapping()
	public String getSource(@RequestHeader("Accept-Language") String locale) {
		return i18n.messageSource().getMessage("hello", null, new Locale(locale));
	}
}
