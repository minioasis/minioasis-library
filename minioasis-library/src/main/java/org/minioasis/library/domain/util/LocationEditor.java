package org.minioasis.library.domain.util;

import java.beans.PropertyEditorSupport;

import org.minioasis.library.domain.Location;
import org.minioasis.library.service.LibraryService;

public class LocationEditor extends PropertyEditorSupport {

	private LibraryService service;
	
	public LocationEditor(LibraryService service) {
		super();
		this.service = service;
	}

	public void setService(LibraryService service) {
		this.service = service;
	}

	public void setAsText(String text) throws IllegalArgumentException {
		int id = Integer.parseInt(text);
		Location location = service.getLocation(id);
		setValue(location);
	}
	
}
