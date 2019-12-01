package org.minioasis.library.controller;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.domain.DataType;
import org.minioasis.library.domain.FormData;
import org.minioasis.library.domain.search.FormDataCriteria;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/admin/formdata")
public class FormDataListSearch {

	@Autowired
	private LibraryService service;
	
	@ModelAttribute("dataTypes")
	public DataType[] populateAccountTypes() {
		return DataType.values();	
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") FormDataCriteria criteria, HttpServletRequest request,
			Model model, Pageable pageable) {

		final String data = criteria.getData();
		final DataType type = criteria.getType();
		Page<FormData> page = this.service.findByDataContainingAndType(data, type, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		
		return "formdatas";

	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String biblios(Model model, HttpServletRequest request, Pageable pageable) {

		Page<FormData> page = this.service.findAllFormDatas(pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("criteria", new FormDataCriteria());
	
		return "formdatas";
		
	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
}
