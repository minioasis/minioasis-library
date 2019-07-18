package org.minioasis.library.repository;

import org.minioasis.library.domain.Photo;

public interface PhotoRepository {

	Photo findPatronByIc(String id) throws Exception;
	
	Photo findPatronThumbnailByIc(String id) throws Exception;
	
	Photo findBiblioByIsbn(String isbn) throws Exception;
	
	Photo findBiblioThumbnailByIsbn(String isbn) throws Exception;

}

