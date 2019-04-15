package org.minioasis.library.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.minioasis.library.domain.Series;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/series")
public class SeriesController {

	@Autowired
	private LibraryService service;

	@RequestMapping("/save")
	public String newSeries(Model model) {
		model.addAttribute("series", new Series());
		return "series.form";
	}
	
	@RequestMapping("/save/{id}")
	public String newAndShowSeries(@PathVariable long id, Model model) {
		
		Series done = service.getSeries(id);
		
		model.addAttribute("series", new Series());
		model.addAttribute("done", done);
		return "series.form";
	}	
	
	
	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(@Valid Series series , BindingResult result, ModelMap model) {

		if(result.hasErrors()){	
			return "series.form";			
		} else {		
			
			try{
				this.service.save(series);
			} 
			catch (DataIntegrityViolationException eive)
			{
				result.rejectValue("name","error.not.unique");			
				return "library/series.form";				
			}
			
			return "redirect:/series/save/" + series.getId();
			
		}			
	}
	
	@RequestMapping(value = { "/edit/{id}" }, method = RequestMethod.GET)
	public String edit(@PathVariable("id") long id, Model model) {
		
		Series series = this.service.getSeries(id);
		
		if(series == null) {
			model.addAttribute("error", "ITEM NOT FOUND !");
			return "error";
		}
		
		model.addAttribute("series", series);
		return "series.form";
		
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	public String edit(@ModelAttribute("series") @Valid Series series, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "series.form";
		}
		else 
		{
			try{
				this.service.save(series);
			}
			catch (DataIntegrityViolationException eive)
			{
				result.rejectValue("name","error.not.unique");		
				return "series.form";
			}
			
			return "redirect:/series/save/" + series.getId();
			
		}
		
	}

	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable("id") long id, Model model) {

		Series series = this.service.getSeries(id);
		if(series != null)
			this.service.deleteSeries(id);
		
		model.addAttribute("id", id);
		return "deleted";
		
	}

	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@RequestParam(value = "keyword", required = false) String keyword, Model model, Pageable pageable) {

		Page<Series> page = new PageImpl<Series>(new ArrayList<Series>(), pageable, 0);
		
		if(keyword != null){
			page = this.service.findSeriesByNameContaining(keyword, pageable);
		}

		model.addAttribute("page", page);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pagingType", "search");
		
		return "seriez";

	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String seriez(Model model, Pageable pageable) {

		Page<Series> page = this.service.findAllSeries(pageable);
		
		model.addAttribute("page", page);
		model.addAttribute("pagingType", "list");
		
		return "seriez";		
	}
	
	@RequestMapping(value = "/phase", method = RequestMethod.GET)
	public @ResponseBody List<Series> findSeries(@RequestParam("phase") String phase) {
		
		if(phase != null && !phase.isEmpty()) {
			List<Series> seriez = service.findSeriesByNameContaining(phase);
			return seriez;
		}
		
		return null;
	}
	
}
