package org.minioasis.library.controller;

import java.util.List;

import org.minioasis.library.domain.DataType;
import org.minioasis.library.domain.FormData;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/formdata")
public class FormDataRestSearch {

	@Autowired
	private LibraryService service;
	
	@RequestMapping(value = "/like", method = RequestMethod.GET)
	public @ResponseBody List<FormData> findFormDatas(@RequestParam("data") String data) {

		if(data != null && !data.isEmpty()) {
			List<FormData> formDatas = service.findByDataContainingAndType(data, DataType.JOURNAL_ENTRY_DESP, PageRequest.of(0, 10, Sort.by("data"))).getContent();
			return formDatas;
		}
	
		return null;
	}
}
