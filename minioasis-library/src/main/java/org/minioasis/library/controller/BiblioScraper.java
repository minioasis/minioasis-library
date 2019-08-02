package org.minioasis.library.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

import javax.validation.Valid;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Controller
@RequestMapping("/admin/biblio")
public class BiblioScraper {

	@Autowired
	private LibraryService service;
	
	private static final String GOOGLE_BOOKS_ISBN_SEARCH = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
	private static final String WORLDCAT_ISBN_SEARCH = "http://worldcatlibraries.org/wcpa/isbn/";
	
    // Parser for multiple date formats; 
 
    private static DateTimeFormatter formatter;
     
    static { 
        // Initialize date parser 
        DateTimeParser[] parsers = {  
                DateTimeFormat.forPattern("yyyy").getParser(), 
                DateTimeFormat.forPattern("yyyy-MM").getParser(), 
                DateTimeFormat.forPattern("yyyy-MM-dd").getParser(), 
                DateTimeFormat.forPattern("MMM d, yyyy").getParser()}; 
        formatter = new DateTimeFormatterBuilder().append( null, parsers ).toFormatter(); 
    }

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

	@RequestMapping(value = { "/scraper/worldcat" }, method = RequestMethod.GET)
    private String searchBiblioWithWorldcat(@RequestParam String isbn, Model model) throws Exception { 
		
		if(isbn == null || isbn.isEmpty()) {
			model.addAttribute("msg", "No_BOOK_FOUND");
			return "biblio.isbn.form";
		}
		
		final String _isbn = isbn.replaceAll("[^\\d]", ""); 
		
		Document doc;
		String worldcatUrl = WORLDCAT_ISBN_SEARCH + _isbn;
		Biblio biblio = new Biblio();

		try {

			doc = Jsoup.connect(worldcatUrl).get();

			// title
			Element bibdata = doc.getElementById("bibdata");
			if(bibdata == null){
				model.addAttribute("msg", "No book found in Worldcat :");
				return "biblio.isbn.form";
			}
				
			Elements elements_title = bibdata.getElementsByClass("title");	
			if(elements_title != null){
				if(elements_title.size() > 0){
					Element e = elements_title.get(0);
					if(e != null)
						biblio.setTitle(e.text());
				}
			}

			// author
			Element content = doc.getElementById("details-allauthors");
			if(content != null) {
				
				Elements links = content.getElementsByTag("a");
				
				if(links != null && links.size() > 0) {
					
					String authors = "";
					
					for (Element link : links) {
					  authors += link.text() + ", ";
					}

					authors = authors.substring(0, authors.length()-2) + ".";			
					biblio.setAuthor(authors);
				}				
			}

			
			// publisher
			Element publisher = doc.getElementById("bib-publisher-cell");
			if (publisher != null) {

				Elements elements_publisher = publisher.getElementsByTag("span");

				if (elements_publisher != null) {

					Publisher p = new Publisher();
					if (elements_publisher.size() > 0) {
						Element e = elements_publisher.get(0);
						if (e != null) {
							p.setName(e.text().replace(",", ""));
							biblio.setPublisher(p);
						}
					} else {
						p.setName(publisher.text().replace(",", ""));
						biblio.setPublisher(p);
					}
				}
			}
			
			
			// isbn
			biblio.setIsbn(_isbn);
			
			// subjects
			Element subject = doc.getElementById("subject-terms");
			
			if (subject != null) {

				Elements subjects = subject.getElementsByClass("subject-term");
		
				if (subject != null && subjects.size() > 0) {
					String sub = "";
					for (Element e : subjects) {
						if (e != null) {
							sub += e.text();
						}
					}

					biblio.setSubject(sub);
				}

			}
			

			// series
			Element series = doc.getElementById("bib-hotSeriesTitles-cell");

			if (series != null) {

				Elements elements_series = series.getElementsByTag("a");
				
				if (elements_series != null && elements_series.size() > 0) {
					Series se = new Series();
					for (Element e : elements_series) {
						if (e != null && !e.text().equals("")) {
							se.setName(e.text());
							biblio.setSeries(se);
						}
					}
				}
			}

			model.addAttribute("biblio", biblio);
			
				
		} catch (IOException e) {

			e.printStackTrace();
		}

		model.addAttribute("biblio", biblio);
		
		return "biblio.scraper.form";
	}
	
	// https://www.googleapis.com/books/v1/volumes?q=isbn:9780465024933 The Feynman Lectures on Physics
	// https://www.googleapis.com/books/v1/volumes?q=isbn:9789571374017  ä½•æ™‚è¦�å¾žçœ¾ï¼Ÿä½•æ™‚å�ˆè©²ç‰¹ç«‹ç�¨è¡Œï¼Ÿ
	
	@RequestMapping(value = { "/scraper/google" }, method = RequestMethod.GET)
    private String searchBiblioWithGoogle(@RequestParam(value = "isbn", required = true) String isbn, Model model) throws Exception { 

		if(isbn == null || isbn.isEmpty()) {
			model.addAttribute("msg", "No_BOOK_FOUND");
			return "biblio.isbn.form";
		}
		
    	final String _isbn = isbn.replaceAll("[^\\d]", ""); 
    	
        URL url = new URL(GOOGLE_BOOKS_ISBN_SEARCH + _isbn);
        //System.out.println(GOOGLE_BOOKS_SEARCH_ISBN + _isbn);
        URLConnection connection = url.openConnection();
        
        connection.setRequestProperty("Accept-Charset", "utf-8"); 
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.62 Safari/537.36"); 
        connection.setConnectTimeout(10000); 
        connection.setReadTimeout(10000); 
        
        InputStream inputStream = connection.getInputStream(); 
        ObjectMapper mapper = new ObjectMapper(); 
        JsonNode rootNode = mapper.readValue(inputStream, JsonNode.class); 
        ArrayNode items = (ArrayNode) rootNode.get("items"); 
        
        Biblio biblio = new Biblio();
        
        if (rootNode.get("totalItems").intValue() <= 0) { 
    		model.addAttribute("errorMsg", "No book found in Google Book API : " + _isbn);
    		model.addAttribute("biblio", biblio);
    		return "biblio.scraper.form"; 
        } 
           
        JsonNode item = items.get(0); 
        JsonNode volumeInfo = item.get("volumeInfo"); 
        
        // title
        biblio.setTitle(volumeInfo.has("title") ? volumeInfo.get("title").asText() : null); 
        
        // author
        ArrayNode authors = (ArrayNode) volumeInfo.get("authors");
        int authorSize = authors.size();
        
        String authorz = "";
        
        if (authorSize <= 0) { 
           biblio.setAuthor(null);
        } else {
        	
        	for(int i = 0; i < authorSize; i++) {
        		String a = authors.get(i).asText();
        		if(i < authorSize-1) {
        			authorz += a + ", "; 
        			
        		}else {
        			authorz += a;
        		}
        	}    	
        }
        
        biblio.setAuthor(authorz);
        
        // publisher
        String p = volumeInfo.has("publisher") ? volumeInfo.get("publisher").asText() : null;
        
        if(p != null && !p.isEmpty()) {
        	Publisher publisher = new Publisher(p);
            biblio.setPublisher(publisher);
        }else {
        	biblio.setPublisher(null);
        }
        
        // isbn
        biblio.setIsbn(_isbn);
        
        // language
        String language = volumeInfo.has("language") ? volumeInfo.get("language").asText() : null;
        
        switch (language) {
        case "en":
        	biblio.setLanguage(Language.en);
            break;
        case "zh-CN":
        	biblio.setLanguage(Language.cn);
            break;
        case "zh-TW":
        	biblio.setLanguage(Language.cn);
            break;
        case "ms":
        	biblio.setLanguage(Language.my);
            break;
        default: 
        	biblio.setLanguage(Language.ot);
            break;
        }

        // pages
        biblio.setPages(volumeInfo.has("pageCount") ? volumeInfo.get("pageCount").asInt() : null); 
        
        // publishingYear
        if(volumeInfo.has("publishedDate")) {
        	Date publishingYear = formatter.parseDateTime(volumeInfo.has("publishedDate") ? volumeInfo.get("publishedDate").asText() : null).toDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(publishingYear);
            int year = cal.get(Calendar.YEAR);  
    	    biblio.setPublishingYear(year);
        }        
         
        // Download the thumbnail 
/*        JsonNode imageLinks = volumeInfo.get("imageLinks"); 
        if (imageLinks != null && imageLinks.has("thumbnail")) { 
            String imageUrl = imageLinks.get("thumbnail").getTextValue(); 
            downloadThumbnail(book, imageUrl); 
        } */
         
	    model.addAttribute("biblio", biblio);
	    
	    return "biblio.scraper.form";
    } 
	
	@RequestMapping(value = { "/scraper/save" }, method = RequestMethod.POST)
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
					return "biblio.scraper.form";
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
					return "biblio.scraper.form";
				}
			}			
			
		}else {
			biblio.setSeries(null);
		}
		
		// save
		if (result.hasErrors()) {
			return "biblio.scraper.form";
		} else {
			
			try{
				this.service.save(biblio);	
			}catch(DataIntegrityViolationException ex){
				return "error";
			}
			
			return "redirect:/admin/biblio/" + biblio.getId();
			
		}		
	}
    
}
