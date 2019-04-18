package org.minioasis.library.controller;

import javax.validation.Valid;

import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.BiblioType;
import org.minioasis.library.domain.Binding;
import org.minioasis.library.domain.Language;
import org.minioasis.library.domain.Publisher;
import org.minioasis.library.domain.Series;
import org.minioasis.library.domain.validator.BiblioValidator;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/biblio")
//@SessionAttributes("biblio")
public class BiblioController {
	
	@Autowired
	private LibraryService service;

	@ModelAttribute("biblioTypes")
	public BiblioType[] populateBiblioTypes(){
		return BiblioType.values();
	}
	@ModelAttribute("languages")
	public Language[] populateLanguages() {
		return Language.values();	
	}
	
	@ModelAttribute("bindings")
	public Binding[] populateBindings() {
		return Binding.values();	
	}
	
	@RequestMapping(value = { "/save" }, method = RequestMethod.GET)
	public String add(Model model) {

		Biblio biblio = new Biblio();

		model.addAttribute("biblio", biblio);
		return "biblio.form";

	}

	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String add(@ModelAttribute("biblio") @Valid Biblio biblio , BindingResult result) {

		String pname = biblio.getPublisher().getName();
		String sname = biblio.getSeries().getName();
		
		// publisher
		if(pname != null && !pname.isEmpty()) {
			
			Publisher p = this.service.findPublisherByName(pname);

			if(p != null) {
				biblio.setPublisher(p);
			} else {
				
				biblio.setPublisher(new Publisher(pname));
				new BiblioValidator().validate(biblio, result);

				if (result.hasErrors()) {
					return "biblio.form";
				}	

			}
			
					
		}else {
			biblio.setPublisher(null);
		}
		
		// series
		if(sname != null && !sname.isEmpty()) {

			Series s = this.service.findSeriesByName(sname);
			
			if(s != null) {
				biblio.setSeries(s);
			}else {
				
				biblio.setSeries(new Series(sname));
				new BiblioValidator().validate(biblio, result);

				if (result.hasErrors()) {
					return "biblio.form";
				}
			}			
			
		}else {
			biblio.setSeries(null);
		}
		
		if (result.hasErrors()) {
			return "biblio.form";
		} else {
			
			try{
				this.service.save(biblio);	
			}catch(DataIntegrityViolationException ex){
				return "error";
			}
			
			return "redirect:/biblio/" + biblio.getId();
			
		}		
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", required = true) long id , Model model) {

		Biblio biblio = this.service.getBiblio(id);
		if(biblio == null)
			return "error";

		model.addAttribute("biblio", biblio );
		return "biblio.form";

	}

	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	public String edit(@ModelAttribute("biblio") @Valid Biblio biblio , BindingResult result) {

		String pname = biblio.getPublisher().getName();
		String sname = biblio.getSeries().getName();
		
		// publisher
		if(pname != null && !pname.isEmpty()) {
			
			Publisher p = this.service.findPublisherByName(pname);

			if(p != null) {
				biblio.setPublisher(p);
			} else {
				
				biblio.setPublisher(new Publisher(pname));
				new BiblioValidator().validate(biblio, result);

				if (result.hasErrors()) {
					return "biblio.form";
				}	

			}
			
					
		}else {
			biblio.setPublisher(null);
		}
		
		// series
		if(sname != null && !sname.isEmpty()) {

			Series s = this.service.findSeriesByName(sname);
			
			if(s != null) {
				biblio.setSeries(s);
			}else {
				
				biblio.setSeries(new Series(sname));
				new BiblioValidator().validate(biblio, result);

				if (result.hasErrors()) {
					return "biblio.form";
				}
			}			
			
		}else {
			biblio.setSeries(null);
		}
		
		if (result.hasErrors()) {
			return "biblio.form";
		} else {
			
			try{
				this.service.save(biblio);
			}catch(DataIntegrityViolationException ex){
				return "error";
			}
			
			return "redirect:/biblio/" + biblio.getId();
			
		}		
	}
	
	@RequestMapping(value = {"/upload" }, method = RequestMethod.GET)
	public String uploadImage(@RequestParam(value = "id", required = true) long id, Model model) {

		Biblio biblio = this.service.getBiblio(id);
		model.addAttribute("biblio", biblio);
		
		return "biblio.image.upload.form";
	}
	
	@RequestMapping(value = { "/upload" }, method = RequestMethod.POST)
	public String uploadImage(@ModelAttribute("biblio") Biblio biblio, @RequestParam(value = "id", required = true) long id, Model model) {

		biblio.setId(id);
		this.service.upload(biblio);

		return "redirect:/biblio/" + biblio.getId();
	}

}