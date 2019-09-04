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
	
	public Photo findBiblioByImageId(String imageId) throws Exception {
		return photoRepository.findBiblioByImageId(imageId);
	}
	
	public Photo findBiblioThumbnailByImageId(String imageId) throws Exception {
		return photoRepository.findBiblioThumbnailByImageId(imageId);
	}

}
