package org.minioasis.library.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.minioasis.library.domain.Holiday;
import org.minioasis.library.domain.search.HolidayCriteria;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/admin/holiday")
public class HolidayController {

	@Autowired
	private LibraryService service;

	@ModelAttribute("finez")
	public List<Boolean> populateFines() {

		List<Boolean> trueAndFalse = new ArrayList<Boolean>();
		trueAndFalse.add(Boolean.FALSE);
		trueAndFalse.add(Boolean.TRUE);

		return trueAndFalse;
	}

	@RequestMapping("/save")
	public String save(Model model) {
		model.addAttribute("holiday", new Holiday());
		return "holiday.form";
	}

	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(@Valid Holiday holiday, BindingResult result) {

		if (result.hasErrors()) {
			return "holiday.form";
		} else {

			LocalDate start = holiday.getStartDate();
			LocalDate end = holiday.getEndDate();

			// check 1
			if (start.isAfter(end)) {
				result.rejectValue("startDate", "", "error.end.date.<.start.date");
				return "holiday.form";
			}

			List<Holiday> holidays = this.service.findByExcluded(start, end);
			int hit = holidays.size();	

			// check 2
			if (hit > 0) {
				result.rejectValue("startDate", "", "error.period.intersect");
				return "holiday.form";
			}

			try {
				this.service.save(holiday);
			} catch (DataIntegrityViolationException eive) {
				result.rejectValue("name", "error.not.unique");
				return "holiday.form";
			}

			return "redirect:/admin/holiday/save/" + holiday.getId();

		}
	}
	
	@RequestMapping("/save/{id}")
	public String save(@PathVariable long id, Model model) {

		Holiday done = service.getHoliday(id);

		model.addAttribute("holiday", new Holiday());
		model.addAttribute("done", done);
		
		return "holiday.form";
	}

	@RequestMapping(value = { "/save/{id}" }, method = RequestMethod.POST)
	public String save(@PathVariable long id, @Valid Holiday holiday, BindingResult result) {

		if (result.hasErrors()) {
			return "holiday.form";
		} else {

			LocalDate start = holiday.getStartDate();
			LocalDate end = holiday.getEndDate();

			// check 1
			if (start.isAfter(end)) {
				result.rejectValue("startDate", "", "error.end.date.<.start.date");
				return "holiday.form";
			}

			List<Holiday> holidays = this.service.findByExcluded(start, end);
			int hit = holidays.size();	

			// check 2
			if (hit > 0) {
				result.rejectValue("startDate", "", "error.period.intersect");
				return "holiday.form";
			}

			try {
				this.service.save(holiday);
			} catch (DataIntegrityViolationException eive) {
				result.rejectValue("name", "error.not.unique");
				return "holiday.form";
			}

			return "redirect:/admin/holiday/save/" + holiday.getId();

		}
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", required = true) long id, Model model) {

		Holiday h = this.service.getHoliday(id);

		if (h == null) {
			model.addAttribute("error", "ITEM NOT FOUND !");
			return "error";
		}

		model.addAttribute("holiday", h);
		
		return "holiday.form";

	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	public String edit(@Valid Holiday holiday, BindingResult result) {

		if (result.hasErrors()) {
			return "holiday.form";
		} else {

			LocalDate start = holiday.getStartDate();
			LocalDate end = holiday.getEndDate();

			// check 1
			if (start.isAfter(end)) {
				result.rejectValue("startDate", "", "error.end.date.<.start.date");
				return "holiday.form";
			}

			List<Holiday> holidays = this.service.findByExcluded(start, end);
			
			if(holidays.contains(holiday)){
				holidays.remove(holiday);
			}
			
			int hit = holidays.size();	

			// check 2
			if (hit > 0) {
				result.rejectValue("startDate", "", "error.period.intersect");
				return "holiday.form";
			}

			try {
				this.service.save(holiday);
			} catch (DataIntegrityViolationException eive) {
				result.rejectValue("name", "error.not.unique");
				return "holiday.form";
			}

			return "redirect:/admin/holiday/save/" + holiday.getId();

		}
	}

	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable("id") long id, Model model) {

		Holiday holiday = this.service.getHoliday(id);
		if (holiday != null)
			this.service.deleteHoliday(id);

		model.addAttribute("id", id);
		return "deleted";

	}

	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") HolidayCriteria criteria, HttpServletRequest request, 
			Model model, Pageable pageable) {

		Page<Holiday> page = this.service.findByCriteria(criteria, pageable);

		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);

		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);

		return "holidays";

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String holidays(Model model, HttpServletRequest request, Pageable pageable) {

		Page<Holiday> page = this.service.findAllHolidays(pageable);

		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("criteria", new HolidayCriteria());
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		
		return "holidays";

	}

	private String buildUri(HttpServletRequest request, int page) {
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request).replaceQueryParam("page", "{id}").build()
				.expand(page);

		return uc.toUriString();
	}

}
