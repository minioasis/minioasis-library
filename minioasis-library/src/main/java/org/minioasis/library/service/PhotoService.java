package org.minioasis.library.service;

import org.minioasis.library.domain.Photo;

public interface PhotoService {

	Photo findPatronByIc(String id) throws Exception;
	
	Photo findPatronThumbnailByIc(String id) throws Exception;
	
	Photo findBiblioByImageId(String imageId) throws Exception;
	
	Photo findBiblioThumbnailByImageId(String imageId) throws Exception;
}
