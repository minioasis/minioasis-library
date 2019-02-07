package org.minioasis.library.domain;

import java.beans.PropertyEditorSupport;

import org.minioasis.library.service.LibraryService;

public class PublicationTypeEditor extends PropertyEditorSupport {
	
	private LibraryService service;
	
	public PublicationTypeEditor(LibraryService service) {
		super();
		this.service = service;
	}

	public void setService(LibraryService service) {
		this.service = service;
	}

	public void setAsText(String text) throws IllegalArgumentException {

		long id = Long.parseLong(text);
		PublicationType publicationType = service.getPublicationType(id);
		setValue(publicationType);
	}
	
}
