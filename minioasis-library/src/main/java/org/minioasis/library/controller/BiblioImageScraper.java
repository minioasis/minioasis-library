package org.minioasis.library.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.BiblioType;
import org.minioasis.library.domain.Binding;
import org.minioasis.library.domain.Language;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.search.BiblioCriteria;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/admin/biblio.img")
public class BiblioImageScraper {

	private static final Log logger = LogFactory.getLog(BiblioImageScraper.class);
	
	//private static final String GOOGLE_BOOKS_ISBN_SEARCH = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
	//private static final String WORLDCAT_ISBN_SEARCH = "http://worldcatlibraries.org/wcpa/isbn/";
	private static final String DOUBAN_ISBN_SEARCH = "https://book.douban.com/isbn/";
	
	@Value("${image.destination.folder}")
	private String IMAGE_DESTINATION_FOLDER;
	
	@Autowired
	private LibraryService service;

	@ModelAttribute("langs")
	public Language[] populateLanguage() {
		return Language.values();	
	}
	
	@ModelAttribute("ats")
	public YesNo[] populateActives() {
		return YesNo.values();	
	}
	
	@ModelAttribute("binds")
	public Binding[] populateBindinds() {
		return Binding.values();	
	}

	@ModelAttribute("bts")
	public BiblioType[] populateBiblioTypes() {
		return BiblioType.values();	
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") BiblioCriteria criteria, HttpServletRequest request, Map<String,String> params, 
			Model model, Pageable pageable) {

		Page<Biblio> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("pagingType", "search");
		
		return "biblios.img";

	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String biblios(Model model, Pageable pageable) {

		Page<Biblio> page = this.service.findAllBiblios(pageable);
		
		model.addAttribute("page", page);
		model.addAttribute("criteria", new BiblioCriteria());
		model.addAttribute("pagingType", "list");
		
		return "biblios.img";
		
	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}

	@RequestMapping(value = { "/douban" }, method = RequestMethod.GET)
	public String scrapingDoubanImage(@RequestParam(value="isbn") String isbn) {
		
		getImage(isbn);
		
		return "redirect:/admin/biblio.img/list?page=0&size=10&sort=updated,desc";
		
	}
	
	@RequestMapping(value = { "/douban" }, method = RequestMethod.POST)
	public String scrapingDoubanImage(@RequestParam(value="isbn") String[] isbn) {
		
		for(int i = 0; i < isbn.length ; i++) {
			getImage(isbn[i]);
		}
		
		return "redirect:/admin/biblio.img/list?page=0&size=10&sort=updated,desc";
		
	}
	
	private void getImage(String isbn) {
		
		String url = DOUBAN_ISBN_SEARCH + isbn;

		Document doc;
		Elements elements;

		try {
			
			logger.info("★★★ " + LocalDateTime.now() + " ★★★" + " Connecting DOUBAN");

			doc = Jsoup.connect(url)
						.timeout(20000)
						.get();
			
			logger.info("★★★ " + LocalDateTime.now() + " ★★★" + " Connected");
			
			elements = doc.getElementsByClass("nbg");

			if (!elements.isEmpty()) {

				String imageURL = elements.first().attr("href");

				downloadImage(imageURL,isbn);

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void downloadImage(String strImageURL, String isbn) {

		logger.info("★★★ " + LocalDateTime.now() + " ★★★" +" Saving : " + isbn + ", from: " + strImageURL);

		try {

			// open the stream from URL
			URL urlImage = new URL(strImageURL);
			InputStream in = urlImage.openStream();

			byte[] buffer = new byte[4096];
			int n = -1;

			OutputStream os = new FileOutputStream(IMAGE_DESTINATION_FOLDER + "/" + isbn + ".jpg");

			// write bytes to the output stream
			while ((n = in.read(buffer)) != -1) {
				os.write(buffer, 0, n);
			}

			// close the stream
			os.close();

			logger.info("★★★ " + LocalDateTime.now() + " ★★★" +" Image saved in " + IMAGE_DESTINATION_FOLDER );

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
