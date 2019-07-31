package org.minioasis.library.controller;

import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.minioasis.library.domain.search.Shelfmark;
import org.minioasis.library.service.ShelfmarkFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("shelfmarks")
@RequestMapping("/admin/shelfmark")
public class ShelfmarksPrinter {

	@Value("${shelfmark.column}")
	private long column;
	
	@Autowired
	private ShelfmarkFormat format;
	
	private long roundUp(long num, long divisor) {
	    return (num + divisor - 1) / divisor;
	}
	
	@RequestMapping(value = "/print", method = RequestMethod.GET)
	public String print(@ModelAttribute("shelfmarks") Set<Shelfmark> shelfmarks, Model model) {
		
		Set<Shelfmark> formatedShelfmark = format.format(shelfmarks);
		
		int total = formatedShelfmark.size();
		
		long rowsSize = roundUp(total,column);
		
		List<List<Shelfmark>> rowlists = new ArrayList<List<Shelfmark>>();
		
		for(int i = 0 ; i < rowsSize ; i++) {
			
			List<Shelfmark> rows = null;

			rows = formatedShelfmark.stream()
								.skip(column * i)
								.limit(column * (i+1))
								.collect(toCollection(ArrayList::new));
			
			
			
			rowlists.add(rows);
			
		}
		
		model.addAttribute("rowlists", rowlists);
		
		return "shelfmarks.formated";
		
	}
	
}
