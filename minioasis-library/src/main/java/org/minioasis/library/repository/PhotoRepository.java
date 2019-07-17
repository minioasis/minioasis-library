package org.minioasis.library.repository;

import org.minioasis.library.domain.Photo;

public interface PhotoRepository {

	Photo findPatronByIc(String id) throws Exception;
	
	Photo findPatronThumbnailByIc(String id) throws Exception;

}

