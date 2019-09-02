package org.minioasis.library.service;

import org.minioasis.library.domain.Photo;
import org.minioasis.library.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoServiceImpl implements PhotoService {
	
	@Autowired
	private PhotoRepository photoRepository;
	
	public Photo findPatronByIc(String ic) throws Exception {
		return photoRepository.findPatronByIc(ic);
	}
	
	public Photo findPatronThumbnailByIc(String ic) throws Exception {
		return photoRepository.findPatronThumbnailByIc(ic);
	}
	
	public Photo findBiblioByIsbn(String isbn) throws Exception {
		return photoRepository.findBiblioByIsbn(isbn);
	}
	
	public Photo findBiblioThumbnailByIsbn(String isbn) throws Exception {
		return photoRepository.findBiblioThumbnailByIsbn(isbn);
	}

	public Photo findJournalByIssnCoden(String id) throws Exception {
		return photoRepository.findJournalByIssnCoden(id);
	}
	
	public Photo findJournalThumbnailByIssnCoden(String id) throws Exception {
		return photoRepository.findJournalThumbnailByIssnCoden(id);
	}
}
