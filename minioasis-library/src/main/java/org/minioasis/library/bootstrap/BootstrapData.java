package org.minioasis.library.bootstrap;

import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.BiblioType;
import org.minioasis.library.domain.Language;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.service.LibraryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class BootstrapData implements CommandLineRunner {

	private final LibraryService service;

	public BootstrapData(LibraryService service) {
		this.service = service;
	}
	
	public void run(String... args) throws Exception {
		
		Biblio b1 = new Biblio();
		b1.setTitle("X-Man");
		b1.setActive(YesNo.Y);
		b1.setBiblioType(BiblioType.BOOK);
		b1.setLanguage(Language.cn);
		service.save(b1);
		
		Biblio b2 = new Biblio();
		b2.setTitle("Spider Man");
		b2.setActive(YesNo.Y);
		b2.setBiblioType(BiblioType.BOOK);
		b2.setLanguage(Language.cn);
		service.save(b2);
		
		Biblio b3 = new Biblio();
		b3.setTitle("Iron Man");
		b3.setActive(YesNo.Y);
		b3.setBiblioType(BiblioType.BOOK);
		b3.setLanguage(Language.cn);
		service.save(b3);
		
	}
}
