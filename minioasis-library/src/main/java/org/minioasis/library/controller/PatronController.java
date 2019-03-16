package org.minioasis.library.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.GroupEditor;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.PatronType;
import org.minioasis.library.domain.Photo;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

@Controller
@RequestMapping("/patron")
public class PatronController {

	@Autowired
	private LibraryService service;	
	
	@ModelAttribute("groups")
	public List<Group> populateGroups() {
		return this.service.findAllGroups(new Sort(Sort.Direction.ASC, "name"));	
	}
	
	@ModelAttribute("patronTypes")
	public List<PatronType> populatePatronTypes() {
		return this.service.findAllPatronTypes(new Sort(Sort.Direction.ASC, "name"));	
	}
	
	@RequestMapping(value = { "/patron.form" }, method = RequestMethod.GET)
	public String create(Model model) {

		model.addAttribute("patron", new Patron());
		return "patron.form";
			
	}
	
	@RequestMapping(value = { "/patron.form" }, method = RequestMethod.POST)
	public String create(@Valid Patron patron, BindingResult result) {

		if(result.hasErrors()){
			//System.out.println("********************" + result.getErrorCount() + "---" + result.toString());
			return "patron.form";			
		} else {
			
			try{
				this.service.save(patron);
			}
			catch (DataIntegrityViolationException eive)
			{
				result.rejectValue("cardKey","","cardkey or entangled not unique");	
				return "patron.form";
			}
			
			return "redirect:/patron/" + patron.getId() + "/upload";
			
		}			
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", required = true) long id, Model model) {

		Patron p = this.service.getPatron(id);
		
		if(p == null) {
			model.addAttribute("error", "ITEM NOT FOUND !");
			return "error";
		}
		
		model.addAttribute("patron", p);
		return "patron.form";
		
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	public String edit(@ModelAttribute("patron") @Valid Patron patron, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			return "patron.form";
		}
		else 
		{
			try{
				this.service.edit(patron);
			}
			catch (DataIntegrityViolationException eive)
			{
				result.rejectValue("cardKey","","cardkey or entangled not unique");				
				return "patron.form";
			}
			
			return "redirect:/patron/" + patron.getId() + "/upload";
			
		}
		
	}
	
	@RequestMapping(value = {"/{id}/upload" }, method = RequestMethod.GET)
	public String uploadImageGet(@PathVariable("id") long id, Model model) {

		model.addAttribute("id", id);
		
		return "patron.upload.form";
	}
	
	@RequestMapping(value = { "/{id}/upload" }, method = RequestMethod.POST)
	public String uploadImagePost(MultipartHttpServletRequest request, @ModelAttribute("photo") Photo photo, @PathVariable("id") long id, Model model) throws IOException {
		System.out.println("---------photo img ------------" + photo.getImg());
		//System.out.println("---------photo img .length------------" + photo.getImg().length);
		System.out.println("---------photo name ------------" + photo.getName());
		
		System.out.println("--------- request.getParameter(\"file\") ------------>>>>" + request.getParameter("file"));
		System.out.println("--------- request.getParameter(\"img\") XXXXXXXXXXXXXXXXXXXXXXXXXXX>>>>" + request.getParameter("img"));
		System.out.println("--------- request.getParameter(\"img\") ooooooooooooooooooooooooooo>>>>" + request.getAttribute("img"));
		
		Enumeration<String> en = request.getParameterNames();
		while (en.hasMoreElements()) {
			String e = en.nextElement();
			System.out.println("......" + e);
		}

		// Getting uploaded files from the request object
        Map<String, MultipartFile> fileMap = request.getFileMap();
		
        // Iterate through the map
        for (MultipartFile multipartFile : fileMap.values()) {
        	System.out.println("..................getName...................." + multipartFile.getName());
        	System.out.println("..................getOriginalFilename...................." + multipartFile.getOriginalFilename());
        	System.out.println("..................getBytes().length...................." + multipartFile.getBytes().length);
        	//photo.setImg(multipartFile.getBytes());
        }
        for (String key : fileMap.keySet()) {
        	System.out.println("..................key...................." + key);
        }
        
		Iterator<String> itr = request.getFileNames();

		while (itr.hasNext()) {
		    String uploadedFile = itr.next();
		    MultipartFile file = request.getFile(uploadedFile);
		    
		    System.out.println("--------- file.getOriginalFilename() ------------" + file.getOriginalFilename());
		}
		
		System.out.println("Docs uploaded !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		Patron patron = this.service.getPatron(id);
		patron.setPhoto(photo);
		this.service.upload(patron);

		return "redirect:/patron/" + patron.getId();
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {

		//Locale locale = request.getLocale();
		StringTrimmerEditor emptyTrimmer = new StringTrimmerEditor(true);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));		
		binder.registerCustomEditor(String.class, null, emptyTrimmer);
		binder.registerCustomEditor(Group.class, new GroupEditor(service));
		binder.registerCustomEditor(byte[].class,new ByteArrayMultipartFileEditor());
/*		binder.registerCustomEditor(Set.class,"patronTypes", new CustomCollectionEditor(Set.class) {
	           protected Object convertElement(Object element) {
	               if (element != null) {
	                   String id = (String) element;
	                   PatronType patronType = service.getPatronType(id);
	                   return patronType;
	               }
	               return null;
	           }

		});*/

	}
	
}
